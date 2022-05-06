/* global d3, self, Intl */
const sunBurst = {};
(() => {
    let current, data, svg;
    let hoverLbl, legendTbl;
    const width = 500,
        height = 500,
        radius = (Math.min(width, height) / 2),
        x = d3.scaleLinear()
        .range([0, 2 * Math.PI]),
        y = d3.scaleSqrt()
        .range([0, radius]),
        partition = d3.partition(),
        arc = d3.arc()
        .startAngle(d => Math.max(0, Math.min(2 * Math.PI, x(d.x0))))
        .endAngle((d) => Math.max(0, Math.min(2 * Math.PI, x(d.x1))))
        .innerRadius(d => Math.max(0, y(d.y0)))
        .outerRadius(d => Math.max(0, y(d.y1))),
        nF = new Intl.NumberFormat('pt-br', {style: 'currency', minimumFractionDigits: 2, maximumFractionDigits: 2, currency: 'BRL'}).format,
        pF = new Intl.NumberFormat('pt-br', {style: 'percent', maximumFractionDigits: 1}).format;
    function start(root, selector) {
        svg = d3.select(selector)
            .attr("width", width)
            .attr("height", height)
            .attr("style", "cursor: crosshair;")
            .append("g")
            .attr("transform", "translate(" + width / 2 + "," + (height / 2) + ")");

        data = root = d3.hierarchy(preProcess(root));
        root.sum(d => d.size);
        if (root.height <= 0) {
            updateStats(null);
            document.querySelector(selector).innerHTML = '';
            return;
        }
        svg.selectAll("path")
            .data(partition(root).descendants())
            .enter().append("path")
            .attr("d", arc)
            .style("fill", d => {
                switch (d.data.name) {
                    case "Não pago":
                        return "#a1051d";
                    case "Pago":
                        return "#09b0c6";
                    default:
                        return d.parent !== null ? hslToRgb(.33, .5, 1 - ((d.value / d.parent.value) / 3 + .33)) : "#3c9462";
                }
            })
            .on("click", clickEvt)
            .attr("data-title", d => d.data.name + '<br>' + nF(d.value) + '<br>' + pF(d.value / (d.parent !== null ? d.parent.value : d.value)));
        clickEvt(root);
        Array.from(document.querySelectorAll('path')).forEach(e => {
            e.addEventListener('mouseenter', () => hoverLbl.innerHTML = e.getAttribute('data-title'), {passive: true});
            e.addEventListener('mousemove', evt => {
                hoverLbl.style.top = evt.y + 'px';
                hoverLbl.style.left = (evt.x + 3) + 'px';
            }, {passive: true});
        });
        d3.select(self.frameElement).style("height", height + "px");
    }
    function clickEvt(d) {
        if (d === null || d.children.length <= 0) {
            return;
        }
        current = d;
        updateStats(d);
        svg.transition().duration(750)
            .tween("scale", () => {
                var xd = d3.interpolate(x.domain(), [d.x0, d.x1]),
                    yd = d3.interpolate(y.domain(), [d.y0, 1]),
                    yr = d3.interpolate(y.range(), [d.y0 ? 20 : 0, radius]);
                return t => {
                    x.domain(xd(t));
                    y.domain(yd(t)).range(yr(t));
                };
            })
            .selectAll("path")
            .attrTween("d", d => () => arc(d));
    }
    function updateStats(level) {
        if (level === null) {
            legendTbl.innerHTML = '<tr><th>Sem dados</th></tr>';
            return;
        }
        legendTbl.innerHTML = `<tr><th>Nome</th><th>Valor</th></tr>
            ${(level.children || []).map(m => `<tr><td>${m.data.name}</td><td>R$ ${nF(m.value)}</td></tr>`).join('')}
            <tr><td>Total</td><td>R$ ${nF(level.value)}</td></tr>`;
    }
    function preProcess(elems) {
        elems.children = elems.children.map(m => ({
                name: m.descricao,
                size: 0,
                children: m.children.map(m => ({
                        name: m.descricao,
                        size: 0,
                        children: [
                            {name: "Pago", size: m.valorPago, children: []},
                            {name: "Não pago", size: (m.valor - m.valorPago), children: []}
                        ]
                    }))
            }));
        return elems;
    }
    sunBurst.start = start;
    sunBurst.clickEvt = clickEvt;
    sunBurst.updateStats = updateStats;

    sunBurst.setHoverLbl = val => hoverLbl = val;
    sunBurst.setLegendTbl = val => legendTbl = val;

    sunBurst.getHoverLbl = () => hoverLbl;
    sunBurst.getLegendTbl = () => legendTbl;

    sunBurst.getData = () => data;
    sunBurst.getCurrent = () => current;
})();

