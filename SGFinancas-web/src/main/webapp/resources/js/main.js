/* global Intl, PrimeFaces, PF, screenBlocker, uploadIndicator, processIndicator */

function loadStatsPrices() {
    const dF = new Intl.NumberFormat('pt-br', {type: 'currency', minimumFractionDigits: 2, maximumFractionDigits: 2});
    Array.from(document.querySelectorAll('.card.overview-box .statistics'))
        .forEach(e => iterator(0, parseFloat(e.getAttribute('data-value')), 3, val => e.innerHTML = 'R$ ' + dF.format(val)));
}

function iterator(startValue, endValue, time, callBack) {
    const frameRate = 20;
    const step = endValue / frameRate;
    const steps = endValue / step * time;
    let frame = 0;
    if (step > 0) {
        const interval = setInterval(() => {
            callBack(startValue + (frame++ * step / time));
            if (frame >= steps) {
                clearInterval(interval);
                callBack(endValue);
            }
        }, frameRate);
    } else {
        callBack(endValue);
    }
}

function lockScreen() {
    screenBlocker.style.display = 'block';
    setTimeout(() => screenBlocker.classList.add('visible'), 0);
}

function unlockScreen() {
    screenBlocker.classList.remove('visible');
    setTimeout(() => screenBlocker.style.display = 'none', 400);
}

function clearDataTableFilters(id) {
    Array.from(document.querySelectorAll('.ui-column-customfilter')).forEach(filtro => {
        const select = filtro.querySelector('select');
        if (select !== null) {
            option = select.querySelector('option');
            select.value = option.value;
            filtro.querySelector('.ui-selectonemenu-label').innerHTML = option.innerHTML;
        } else {
            Array.from(filtro.querySelectorAll('input')).forEach(e => e.value = '');
        }
    });
    PF(id).clearFilters();
    PF(id).filter();
}

function initDataTableFilterFix() {
    if (PF('tbl') !== undefined) {
        if (PF('tbl').cfg.behaviors === null || PF('tbl').cfg.behaviors === undefined) {
            PF('tbl').cfg.behaviors = {};
        }
        const oldFilter = PF('tbl').cfg.behaviors.filter;
        PF('tbl').cfg.behaviors.filter = ext => {
            const oldOnComplete = ext.oncomplete;
            ext.oncomplete = (data, statusText, moreData) => {
                const elem = Array.from(document.querySelectorAll('.ui-datatable.ui-widget'))
                    .filter(f => f.id === ext.update)[0]
                    .querySelector('.ui-datatable-footer');
                if (elem !== undefined && elem !== null) {
                    elem.innerHTML = 'Total de registros: ' + moreData.totalRecords;
                }
                if (hasApplyedDataTableContextMenu) {
                    initDataTableContextMenu();
                }
                oldOnComplete(data, statusText, moreData);
            };
            PrimeFaces.ajax.Request.handle(ext);
            if (typeof oldFilter === 'function') {
                oldFilter(ext);
            }
        };
    }
}

function initDataTableContextMenu() {
    hasApplyedDataTableContextMenu = true;
    Array.from(document.querySelectorAll('.ui-datatable.ui-widget:not(.force-buttons)'))
        .forEach(e => {
            try {
                let headerText = e.querySelector('table > thead > tr > th:last-child');
                if (headerText.innerHTML.indexOf('Ações') !== -1) {
                    headerText.style.display = 'none';
                    let elems = Array.from(e.querySelectorAll('table > tbody > tr'));
                    if (!hasAttachedMutationObsever) {
                        new MutationObserver(evt => {
                            if (evt.some(s => s.addedNodes.length > 0)) {
                                initDataTableContextMenu();
                            }
                        }).observe(elems[0].parentElement, {childList: true});
                        hasAttachedMutationObsever = true;
                    }

                    elems.forEach(e => {
                        let primeira = e.querySelector('td:not(.ui-helper-hidden)');
                        let actions = e.querySelector('td:last-child');
                        primeira.style.overflow = 'visible';
                        if (primeira.querySelector(".hover-menu") === null) {
                            primeira.innerHTML = primeira.innerHTML +
                                '<div class="hover-menu">' +
                                actions.innerHTML +
                                '<div class="clearfix"></div></div>';
                        }
                        actions.style.display = 'none';
                        let canMove = true;
                        const hoverMenu = primeira.querySelector('.hover-menu');
                        hoverMenu.addEventListener('mouseenter', () => canMove = false, {passive: true});
                        hoverMenu.addEventListener('mouseleave', () => canMove = true, {passive: true});
                        hoverMenu.style.left = (e.getBoundingClientRect().left + 15) + 'px';
                        hoverMenu.style.top = (e.getBoundingClientRect().top + primeira.clientHeight) + 'px';
                        e.addEventListener('mousemove', evt => {
                            if (canMove) {
                                const trElemRect = e.getBoundingClientRect();
                                const elemWidth = e.clientWidth;
                                const hoverWidth = hoverMenu.clientWidth;

                                let pos = evt.clientX - trElemRect.left - 30;
                                if (pos < 15) {
                                    pos = 15;
                                }
                                if (pos > elemWidth - hoverWidth - 25) {
                                    pos = elemWidth - hoverWidth - 25;
                                }
                                hoverMenu.style.left = (pos + trElemRect.left) + 'px';
                                hoverMenu.style.top = (trElemRect.top + primeira.clientHeight) + 'px';
                            }
                        }, {passive: true});
                    });
                }
            } catch (e) {
            }
        });
}

function updateDataTableLayout(float) {
    if ('localStorage' in window) {
        localStorage.setItem('useFloatingActionButton', float);
        location.reload();
    } else {
        alert('Não é possível mudar essa configuração, por favor atualize o seu navegador.');
    }
}

function toggleInlineFilter(elem, filter = false) {
    $(elem).parents('th').find('.ui-column-title').children().toggleClass('show hide');
    changeRangeFilter(elem);
    if (filter) {
        PF('tbl').filter();
    }
    return filter;
}

function clearInlineFilter(elem, toggle = true, filter = false) {
    $(elem).parents('th').find('.ui-column-title input').val('');
    if (toggle) {
        toggleInlineFilter(elem, filter);
    }
    return filter;
}

function changeRangeFilter(elem) {
    const inps = $(elem).parents('th').find('.ui-column-customfilter input, .inputs input'),
        inpHidden = inps.get().pop(),
        otherHiddenInps = inps.filter('[type=hideen]'),
        otherTextInps = inps.filter('[type=text]');
    let val = '';
    if (otherHiddenInps.length > 0) {
        val = JSON.stringify(otherHiddenInps.get().map((m, i) => (i === 0 ? 'g' : 'l') + 'e:' + convertType(m)));
    } else {
        val = JSON.stringify(otherTextInps.get().map((m, i) => (i === 0 ? 'g' : 'l') + 'e:' + convertType(m)));
    }
    inpHidden.value = val;
}

function convertType(elem) {
    if (elem.classList.contains('hasDatepicker')) {
        return elem.value.split('/').reverse().join('-');
    } else {
        return elem.value;
    }
}

function updateTipoGrafico(tipo) {
    if ('localStorage' in window) {
        localStorage.setItem('tipografico', tipo);
        loadTipoGrafico();
    }
}

function loadTipoGrafico() {
    if ('localStorage' in window) {
        const itens = Array.from(document.querySelectorAll('.simple-advanced .row'));
        const config = localStorage.getItem('tipografico');

        itens.forEach(e => e.classList.remove('d-none'));
        itens[(config === 'A' ? 0 : 1)].classList.add('d-none');
    }
}

function hslToRgb(h, s, l) {
    var r, g, b;

    if (s === 0) {
        r = g = b = l; // achromatic
    } else {
        const hue2rgb = (p, q, t) => {
            if (t < 0)
                t += 1;
            if (t > 1)
                t -= 1;
            if (t < 1 / 6)
                return p + (q - p) * 6 * t;
            if (t < 1 / 2)
                return q;
            if (t < 2 / 3)
                return p + (q - p) * (2 / 3 - t) * 6;
            return p;
        };

        var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
        var p = 2 * l - q;
        r = hue2rgb(p, q, h + 1 / 3);
        g = hue2rgb(p, q, h);
        b = hue2rgb(p, q, h - 1 / 3);
    }

    return "#" + [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)]
        .map(m => m.toString(16).padStart(2, "0"))
        .join("");
}

function showUpload(type = 'file-text-o') {
    lockScreen();
    uploadIndicator.classList.add('active');
    uploadIndicator.querySelector('i.icon').classList = 'icon fa fa-' + type;
}

function hideUpload() {
    unlockScreen();
    uploadIndicator.classList.remove('active');
}

function showProcess(type = 'fa-cog fa-spin') {
    lockScreen();
    processIndicator.classList.add('active');
    processIndicator.querySelector('i.icon').classList = 'icon fa ' + type;
}

function hideProcess() {
    unlockScreen();
    processIndicator.classList.remove('active');
}

let autoSelectItemList = [];

function autoSelectItem(id, value) {
    autoSelectItemList.push({id, value});
}

function triggerAutoSelect() {
    autoSelectItemList.forEach(item => PF(item.id).selectValue(item.value));
    autoSelectItemList = [];
}

function alignItemsEnd() {
    Array.from(document.querySelectorAll('div.row > div.col.form-group:first-child'))
        .forEach(e => e.parentElement.classList.add('align-items-end'));
}

PrimeFaces.locales ['pt_BR'] = {
    closeText: 'Fechar',
    prevText: 'Anterior',
    nextText: 'Próximo',
    monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
    monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
    dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
    dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
    dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
    weekHeader: 'Semana',
    weekNumberTitle: 'Sm',
    firstDay: 0,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Só Horas',
    timeText: 'Tempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    currentText: 'Hoje',
    ampm: false,
    month: 'Mês',
    week: 'Semana',
    day: 'Dia',
    allDayText: 'Todo o Dia',
};