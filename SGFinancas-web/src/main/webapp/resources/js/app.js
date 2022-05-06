/**
Core script to handle the entire theme and core functions
**/
var App = function() {

    // IE mode
    var isRTL = false;
    var isIE8 = false;
    var isIE9 = false;
    var isIE10 = false;

    var resizeHandlers = [];

	if(document.getElementById('contextPath') != null){
        assetsPath = document.getElementById('contextPath').innerHTML + '/resources/metronic/assets/';
    }else{
        assetsPath ="/resources/metronic/assets/";
    }
	
    
    var globalImgPath = 'global/img/';

    var globalPluginsPath = 'global/plugins/';

    var globalCssPath = 'global/css/';

    // theme layout color set

    var brandColors = {
        'blue': '#89C4F4',
        'red': '#F3565D',
        'green': '#1bbc9b',
        'purple': '#9b59b6',
        'grey': '#95a5a6',
        'yellow': '#F8CB00'
    };

    // initializes main settings
    var handleInit = function() {

        if ($m('body').css('direction') === 'rtl') {
            isRTL = true;
        }

        isIE8 = !!navigator.userAgent.match(/MSIE 8.0/);
        isIE9 = !!navigator.userAgent.match(/MSIE 9.0/);
        isIE10 = !!navigator.userAgent.match(/MSIE 10.0/);

        if (isIE10) {
            $m('html').addClass('ie10'); // detect IE10 version
        }

        if (isIE10 || isIE9 || isIE8) {
            $m('html').addClass('ie'); // detect IE10 version
        }
    };

    // runs callback functions set by App.addResponsiveHandler().
    var _runResizeHandlers = function() {
        // reinitialize other subscribed elements
        for (var i = 0; i < resizeHandlers.length; i++) {
            var each = resizeHandlers[i];
            each.call();
        }
    };

    // handle the layout reinitialization on window resize
    var handleOnResize = function() {
        var resize;
        if (isIE8) {
            var currheight;
            $m(window).resize(function() {
                if (currheight == document.documentElement.clientHeight) {
                    return; //quite event since only body resized not window.
                }
                if (resize) {
                    clearTimeout(resize);
                }
                resize = setTimeout(function() {
                    _runResizeHandlers();
                }, 50); // wait 50ms until window resize finishes.                
                currheight = document.documentElement.clientHeight; // store last body client height
            });
        } else {
            $m(window).resize(function() {
                if (resize) {
                    clearTimeout(resize);
                }
                resize = setTimeout(function() {
                    _runResizeHandlers();
                }, 50); // wait 50ms until window resize finishes.
            });
        }
    };

    // Handles portlet tools & actions
    var handlePortletTools = function() {
        // handle portlet remove
        $m('body').on('click', '.portlet > .portlet-title > .tools > a.remove', function(e) {
            e.preventDefault();
            var portlet = $m(this).closest(".portlet");

            if ($m('body').hasClass('page-portlet-fullscreen')) {
                $m('body').removeClass('page-portlet-fullscreen');
            }

            portlet.find('.portlet-title .fullscreen').tooltip('destroy');
            portlet.find('.portlet-title > .tools > .reload').tooltip('destroy');
            portlet.find('.portlet-title > .tools > .remove').tooltip('destroy');
            portlet.find('.portlet-title > .tools > .config').tooltip('destroy');
            portlet.find('.portlet-title > .tools > .collapse, .portlet > .portlet-title > .tools > .expand').tooltip('destroy');

            portlet.remove();
        });

        // handle portlet fullscreen
        $m('body').on('click', '.portlet > .portlet-title .fullscreen', function(e) {
            e.preventDefault();
            var portlet = $m(this).closest(".portlet");
            if (portlet.hasClass('portlet-fullscreen')) {
                $m(this).removeClass('on');
                portlet.removeClass('portlet-fullscreen');
                $m('body').removeClass('page-portlet-fullscreen');
                portlet.children('.portlet-body').css('height', 'auto');
            } else {
                var height = App.getViewPort().height -
                    portlet.children('.portlet-title').outerHeight() -
                    parseInt(portlet.children('.portlet-body').css('padding-top')) -
                    parseInt(portlet.children('.portlet-body').css('padding-bottom'));

                $m(this).addClass('on');
                portlet.addClass('portlet-fullscreen');
                $m('body').addClass('page-portlet-fullscreen');
                portlet.children('.portlet-body').css('height', height);
            }
        });

        $m('body').on('click', '.portlet > .portlet-title > .tools > a.reload', function(e) {
            e.preventDefault();
            var el = $m(this).closest(".portlet").children(".portlet-body");
            var url = $m(this).attr("data-url");
            var error = $m(this).attr("data-error-display");
            if (url) {
                App.blockUI({
                    target: el,
                    animate: true,
                    overlayColor: 'none'
                });
                $m.ajax({
                    type: "GET",
                    cache: false,
                    url: url,
                    dataType: "html",
                    success: function(res) {
                        App.unblockUI(el);
                        el.html(res);
                        App.initAjax() // reinitialize elements & plugins for newly loaded content
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        App.unblockUI(el);
                        var msg = 'Error on reloading the content. Please check your connection and try again.';
                        if (error == "toastr" && toastr) {
                            toastr.error(msg);
                        } else if (error == "notific8" && $m.notific8) {
                            $m.notific8('zindex', 11500);
                            $m.notific8(msg, {
                                theme: 'ruby',
                                life: 3000
                            });
                        } else {
                            alert(msg);
                        }
                    }
                });
            } else {
                // for demo purpose
                App.blockUI({
                    target: el,
                    animate: true,
                    overlayColor: 'none'
                });
                window.setTimeout(function() {
                    App.unblockUI(el);
                }, 1000);
            }
        });

        // load ajax data on page init
        $m('.portlet .portlet-title a.reload[data-load="true"]').click();

        $m('body').on('click', '.portlet > .portlet-title > .tools > .collapse, .portlet .portlet-title > .tools > .expand', function(e) {
            e.preventDefault();
            var el = $m(this).closest(".portlet").children(".portlet-body");
            if ($m(this).hasClass("collapse")) {
                $m(this).removeClass("collapse").addClass("expand");
                el.slideUp(200);
            } else {
                $m(this).removeClass("expand").addClass("collapse");
                el.slideDown(200);
            }
        });
    };

    // Handles custom checkboxes & radios using jQuery Uniform plugin
    var handleUniform = function() {
        if (!$m().uniform) {
            return;
        }
        var test = $m("input[type=checkbox]:not(.toggle, .md-check, .md-radiobtn, .make-switch, .icheck), input[type=radio]:not(.toggle, .md-check, .md-radiobtn, .star, .make-switch, .icheck)");
        if (test.size() > 0) {
            test.each(function() {
                if ($m(this).parents(".checker").size() === 0) {
                    $m(this).show();
                    $m(this).uniform();
                }
            });
        }
    };

    // Handlesmaterial design checkboxes
    var handleMaterialDesign = function() {

        // Material design ckeckbox and radio effects
        $m('body').on('click', '.md-checkbox > label, .md-radio > label', function() {
            var the = $m(this);
            // find the first span which is our circle/bubble
            var el = $m(this).children('span:first-child');
              
            // add the bubble class (we do this so it doesnt show on page load)
            el.addClass('inc');
              
            // clone it
            var newone = el.clone(true);  
              
            // add the cloned version before our original
            el.before(newone);  
              
            // remove the original so that it is ready to run on next click
            $m("." + el.attr("class") + ":last", the).remove();
        }); 

        if ($m('body').hasClass('page-md')) { 
            // Material design click effect
            // credit where credit's due; http://thecodeplayer.com/walkthrough/ripple-click-effect-google-material-design       
            var element, circle, d, x, y;
            $m('body').on('click', 'a.btn, button.btn, input.btn, label.btn', function(e) { 
                element = $m(this);
      
                if(element.find(".md-click-circle").length == 0) {
                    element.prepend("<span class='md-click-circle'></span>");
                }
                    
                circle = element.find(".md-click-circle");
                circle.removeClass("md-click-animate");
                
                if(!circle.height() && !circle.width()) {
                    d = Math.max(element.outerWidth(), element.outerHeight());
                    circle.css({height: d, width: d});
                }
                
                x = e.pageX - element.offset().left - circle.width()/2;
                y = e.pageY - element.offset().top - circle.height()/2;
                
                circle.css({top: y+'px', left: x+'px'}).addClass("md-click-animate");

                setTimeout(function() {
                    circle.remove();      
                }, 1000);
            });
        }

        // Floating labels
        var handleInput = function(el) {
            if (el.val() != "") {
                el.addClass('edited');
            } else {
                el.removeClass('edited');
            }
        } 

        $m('body').on('keydown', '.form-md-floating-label .form-control', function(e) { 
            handleInput($m(this));
        });
        $m('body').on('blur', '.form-md-floating-label .form-control', function(e) { 
            handleInput($m(this));
        });        

        $m('.form-md-floating-label .form-control').each(function(){
            if ($m(this).val().length > 0) {
                $m(this).addClass('edited');
            }
        });
    }

    // Handles custom checkboxes & radios using jQuery iCheck plugin
    var handleiCheck = function() {
        if (!$m().iCheck) {
            return;
        }

        $m('.icheck').each(function() {
            var checkboxClass = $m(this).attr('data-checkbox') ? $m(this).attr('data-checkbox') : 'icheckbox_minimal-grey';
            var radioClass = $m(this).attr('data-radio') ? $m(this).attr('data-radio') : 'iradio_minimal-grey';

            if (checkboxClass.indexOf('_line') > -1 || radioClass.indexOf('_line') > -1) {
                $m(this).iCheck({
                    checkboxClass: checkboxClass,
                    radioClass: radioClass,
                    insert: '<div class="icheck_line-icon"></div>' + $m(this).attr("data-label")
                });
            } else {
                $m(this).iCheck({
                    checkboxClass: checkboxClass,
                    radioClass: radioClass
                });
            }
        });
    };

    // Handles Bootstrap switches
    var handleBootstrapSwitch = function() {
        if (!$m().bootstrapSwitch) {
            return;
        }
        $m('.make-switch').bootstrapSwitch();
    };

    // Handles Bootstrap confirmations
    var handleBootstrapConfirmation = function() {
        if (!$m().confirmation) {
            return;
        }
        $m('[data-toggle=confirmation]').confirmation({ container: 'body', btnOkClass: 'btn btn-sm btn-success', btnCancelClass: 'btn btn-sm btn-danger'});
    }
    
    // Handles Bootstrap Accordions.
    var handleAccordions = function() {
        $m('body').on('shown.bs.collapse', '.accordion.scrollable', function(e) {
            App.scrollTo($m(e.target));
        });
    };

    // Handles Bootstrap Tabs.
    var handleTabs = function() {
        //activate tab if tab id provided in the URL
        if (location.hash) {
            var tabid = encodeURI(location.hash.substr(1));
            $m('a[href="#' + tabid + '"]').parents('.tab-pane:hidden').each(function() {
                var tabid = $m(this).attr("id");
                $m('a[href="#' + tabid + '"]').click();
            });
            $m('a[href="#' + tabid + '"]').click();
        }

        if ($m().tabdrop) {
            $m('.tabbable-tabdrop .nav-pills, .tabbable-tabdrop .nav-tabs').tabdrop({
                text: '<i class="fa fa-ellipsis-v"></i>&nbsp;<i class="fa fa-angle-down"></i>'
            });
        }
    };

    // Handles Bootstrap Modals.
    var handleModals = function() {        
        // fix stackable modal issue: when 2 or more modals opened, closing one of modal will remove .modal-open class. 
        $m('body').on('hide.bs.modal', function() {
            if ($m('.modal:visible').size() > 1 && $m('html').hasClass('modal-open') === false) {
                $m('html').addClass('modal-open');
            } else if ($m('.modal:visible').size() <= 1) {
                $m('html').removeClass('modal-open');
            }
        });

        // fix page scrollbars issue
        $m('body').on('show.bs.modal', '.modal', function() {
            if ($m(this).hasClass("modal-scroll")) {
                $m('body').addClass("modal-open-noscroll");
            }
        });

        // fix page scrollbars issue
        $m('body').on('hide.bs.modal', '.modal', function() {
            $m('body').removeClass("modal-open-noscroll");
        });

        // remove ajax content and remove cache on modal closed 
        $m('body').on('hidden.bs.modal', '.modal:not(.modal-cached)', function () {
            $m(this).removeData('bs.modal');
        });
    };

    // Handles Bootstrap Tooltips.
    var handleTooltips = function() {
        // global tooltips
        $m('.tooltips').tooltip();

        // portlet tooltips
        $m('.portlet > .portlet-title .fullscreen').tooltip({
            container: 'body',
            title: 'Fullscreen'
        });
        $m('.portlet > .portlet-title > .tools > .reload').tooltip({
            container: 'body',
            title: 'Reload'
        });
        $m('.portlet > .portlet-title > .tools > .remove').tooltip({
            container: 'body',
            title: 'Remove'
        });
        $m('.portlet > .portlet-title > .tools > .config').tooltip({
            container: 'body',
            title: 'Settings'
        });
        $m('.portlet > .portlet-title > .tools > .collapse, .portlet > .portlet-title > .tools > .expand').tooltip({
            container: 'body',
            title: 'Collapse/Expand'
        });
    };

    // Handles Bootstrap Dropdowns
    var handleDropdowns = function() {
        /*
          Hold dropdown on click  
        */
        $m('body').on('click', '.dropdown-menu.hold-on-click', function(e) {
            e.stopPropagation();
        });
    };

    var handleAlerts = function() {
        $m('body').on('click', '[data-close="alert"]', function(e) {
            $m(this).parent('.alert').hide();
            $m(this).closest('.note').hide();
            e.preventDefault();
        });

        $m('body').on('click', '[data-close="note"]', function(e) {
            $m(this).closest('.note').hide();
            e.preventDefault();
        });

        $m('body').on('click', '[data-remove="note"]', function(e) {
            $m(this).closest('.note').remove();
            e.preventDefault();
        });
    };

    // Handle Hower Dropdowns
    var handleDropdownHover = function() {
        $m('[data-hover="dropdown"]').not('.hover-initialized').each(function() {
            $m(this).dropdownHover();
            $m(this).addClass('hover-initialized');
        });
    };

    // Handle textarea autosize 
    var handleTextareaAutosize = function() {
        if (typeof(autosize) == "function") {
            autosize(document.querySelector('textarea.autosizeme'));
        }
    }

    // Handles Bootstrap Popovers

    // last popep popover
    var lastPopedPopover;

    var handlePopovers = function() {
        $m('.popovers').popover();

        // close last displayed popover

        $m(document).on('click.bs.popover.data-api', function(e) {
            if (lastPopedPopover) {
                lastPopedPopover.popover('hide');
            }
        });
    };

    // Handles scrollable contents using jQuery SlimScroll plugin.
    var handleScrollers = function() {
        App.initSlimScroll('.scroller');
    };

    // Handles Image Preview using jQuery Fancybox plugin
    var handleFancybox = function() {
        if (!jQuery.fancybox) {
            return;
        }

        if ($m(".fancybox-button").size() > 0) {
            $m(".fancybox-button").fancybox({
                groupAttr: 'data-rel',
                prevEffect: 'none',
                nextEffect: 'none',
                closeBtn: true,
                helpers: {
                    title: {
                        type: 'inside'
                    }
                }
            });
        }
    };

    // Handles counterup plugin wrapper
    var handleCounterup = function() {
        if (!$m().counterUp) {
            return;
        }

        $m("[data-counter='counterup']").counterUp({
            delay: 10,
            time: 1000
        });
    };

    // Fix input placeholder issue for IE8 and IE9
    var handleFixInputPlaceholderForIE = function() {
        //fix html5 placeholder attribute for ie7 & ie8
        if (isIE8 || isIE9) { // ie8 & ie9
            // this is html5 placeholder fix for inputs, inputs with placeholder-no-fix class will be skipped(e.g: we need this for password fields)
            $m('input[placeholder]:not(.placeholder-no-fix), textarea[placeholder]:not(.placeholder-no-fix)').each(function() {
                var input = $m(this);

                if (input.val() === '' && input.attr("placeholder") !== '') {
                    input.addClass("placeholder").val(input.attr('placeholder'));
                }

                input.focus(function() {
                    if (input.val() == input.attr('placeholder')) {
                        input.val('');
                    }
                });

                input.blur(function() {
                    if (input.val() === '' || input.val() == input.attr('placeholder')) {
                        input.val(input.attr('placeholder'));
                    }
                });
            });
        }
    };

    // Handle Select2 Dropdowns
    var handleSelect2 = function() {
        if ($m().select2) {
            $m.fn.select2.defaults.set("theme", "bootstrap");
            $m('.select2me').select2({
                placeholder: "Select",
                width: 'auto', 
                allowClear: true
            });
        }
    };

    // handle group element heights
   var handleHeight = function() {
       $m('[data-auto-height]').each(function() {
            var parent = $m(this);
            var items = $m('[data-height]', parent);
            var height = 0;
            var mode = parent.attr('data-mode');
            var offset = parseInt(parent.attr('data-offset') ? parent.attr('data-offset') : 0);

            items.each(function() {
                if ($m(this).attr('data-height') == "height") {
                    $m(this).css('height', '');
                } else {
                    $m(this).css('min-height', '');
                }

                var height_ = (mode == 'base-height' ? $m(this).outerHeight() : $m(this).outerHeight(true));
                if (height_ > height) {
                    height = height_;
                }
            });

            height = height + offset;

            items.each(function() {
                if ($m(this).attr('data-height') == "height") {
                    $m(this).css('height', height);
                } else {
                    $m(this).css('min-height', height);
                }
            });

            if(parent.attr('data-related')) {
                $m(parent.attr('data-related')).css('height', parent.height());
            }
       });       
    }
    
    //* END:CORE HANDLERS *//

    return {

        //main function to initiate the theme
        init: function() {
            //IMPORTANT!!!: Do not modify the core handlers call order.

            //Core handlers
            handleInit(); // initialize core variables
            handleOnResize(); // set and handle responsive    

            //UI Component handlers     
            handleMaterialDesign(); // handle material design       
            handleUniform(); // hanfle custom radio & checkboxes
            handleiCheck(); // handles custom icheck radio and checkboxes
            handleBootstrapSwitch(); // handle bootstrap switch plugin
            handleScrollers(); // handles slim scrolling contents 
            handleFancybox(); // handle fancy box
            handleSelect2(); // handle custom Select2 dropdowns
            handlePortletTools(); // handles portlet action bar functionality(refresh, configure, toggle, remove)
            handleAlerts(); //handle closabled alerts
            handleDropdowns(); // handle dropdowns
            handleTabs(); // handle tabs
            handleTooltips(); // handle bootstrap tooltips
            handlePopovers(); // handles bootstrap popovers
            handleAccordions(); //handles accordions 
            handleModals(); // handle modals
            handleBootstrapConfirmation(); // handle bootstrap confirmations
            handleTextareaAutosize(); // handle autosize textareas
            handleCounterup(); // handle counterup instances

            //Handle group element heights
            handleHeight();
            this.addResizeHandler(handleHeight); // handle auto calculating height on window resize

            // Hacks
            handleFixInputPlaceholderForIE(); //IE8 & IE9 input placeholder issue fix
        },

        //main function to initiate core javascript after ajax complete
        initAjax: function() {
            handleUniform(); // handles custom radio & checkboxes     
            handleiCheck(); // handles custom icheck radio and checkboxes
            handleBootstrapSwitch(); // handle bootstrap switch plugin
            handleDropdownHover(); // handles dropdown hover       
            handleScrollers(); // handles slim scrolling contents 
            handleSelect2(); // handle custom Select2 dropdowns
            handleFancybox(); // handle fancy box
            handleDropdowns(); // handle dropdowns
            handleTooltips(); // handle bootstrap tooltips
            handlePopovers(); // handles bootstrap popovers
            handleAccordions(); //handles accordions 
            handleBootstrapConfirmation(); // handle bootstrap confirmations
        },

        //init main components 
        initComponents: function() {
            this.initAjax();
        },

        //public function to remember last opened popover that needs to be closed on click
        setLastPopedPopover: function(el) {
            lastPopedPopover = el;
        },

        //public function to add callback a function which will be called on window resize
        addResizeHandler: function(func) {
            resizeHandlers.push(func);
        },

        //public functon to call _runresizeHandlers
        runResizeHandlers: function() {
            _runResizeHandlers();
        },

        // wrApper function to scroll(focus) to an element
        scrollTo: function(el, offeset) {
            var pos = (el && el.size() > 0) ? el.offset().top : 0;

            if (el) {
                if ($m('body').hasClass('page-header-fixed')) {
                    pos = pos - $m('.page-header').height();
                } else if ($m('body').hasClass('page-header-top-fixed')) {
                    pos = pos - $m('.page-header-top').height();
                } else if ($m('body').hasClass('page-header-menu-fixed')) {
                    pos = pos - $m('.page-header-menu').height();
                }
                pos = pos + (offeset ? offeset : -1 * el.height());
            }

            $m('html,body').animate({
                scrollTop: pos
            }, 'slow');
        },

        initSlimScroll: function(el) {
            $m(el).each(function() {
                if ($m(this).attr("data-initialized")) {
                    return; // exit
                }

                var height;

                if ($m(this).attr("data-height")) {
                    height = $m(this).attr("data-height");
                } else {
                    height = $m(this).css('height');
                }

                $m(this).slimScroll({
                    allowPageScroll: true, // allow page scroll when the element scroll is ended
                    size: '7px',
                    color: ($m(this).attr("data-handle-color") ? $m(this).attr("data-handle-color") : '#bbb'),
                    wrapperClass: ($m(this).attr("data-wrapper-class") ? $m(this).attr("data-wrapper-class") : 'slimScrollDiv'),
                    railColor: ($m(this).attr("data-rail-color") ? $m(this).attr("data-rail-color") : '#eaeaea'),
                    position: isRTL ? 'left' : 'right',
                    height: height,
                    alwaysVisible: ($m(this).attr("data-always-visible") == "1" ? true : false),
                    railVisible: ($m(this).attr("data-rail-visible") == "1" ? true : false),
                    disableFadeOut: true
                });

                $m(this).attr("data-initialized", "1");
            });
        },

        destroySlimScroll: function(el) {
            $m(el).each(function() {
                if ($m(this).attr("data-initialized") === "1") { // destroy existing instance before updating the height
                    $m(this).removeAttr("data-initialized");
                    $m(this).removeAttr("style");

                    var attrList = {};

                    // store the custom attribures so later we will reassign.
                    if ($m(this).attr("data-handle-color")) {
                        attrList["data-handle-color"] = $m(this).attr("data-handle-color");
                    }
                    if ($m(this).attr("data-wrapper-class")) {
                        attrList["data-wrapper-class"] = $m(this).attr("data-wrapper-class");
                    }
                    if ($m(this).attr("data-rail-color")) {
                        attrList["data-rail-color"] = $m(this).attr("data-rail-color");
                    }
                    if ($m(this).attr("data-always-visible")) {
                        attrList["data-always-visible"] = $m(this).attr("data-always-visible");
                    }
                    if ($m(this).attr("data-rail-visible")) {
                        attrList["data-rail-visible"] = $m(this).attr("data-rail-visible");
                    }

                    $m(this).slimScroll({
                        wrapperClass: ($m(this).attr("data-wrapper-class") ? $m(this).attr("data-wrapper-class") : 'slimScrollDiv'),
                        destroy: true
                    });

                    var the = $m(this);

                    // reassign custom attributes
                    $m.each(attrList, function(key, value) {
                        the.attr(key, value);
                    });

                }
            });
        },

        // function to scroll to the top
        scrollTop: function() {
            App.scrollTo();
        },

        // wrApper function to  block element(indicate loading)
        blockUI: function(options) {
            options = $m.extend(true, {}, options);
            var html = '';
            if (options.animate) {
                html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '">' + '<div class="block-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>' + '</div>';
            } else if (options.iconOnly) {
                html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '"><img src="' + this.getGlobalImgPath() + 'loading-spinner-grey.gif" align=""></div>';
            } else if (options.textOnly) {
                html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '"><span>&nbsp;&nbsp;' + (options.message ? options.message : 'LOADING...') + '</span></div>';
            } else {
                html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '"><img src="' + this.getGlobalImgPath() + 'loading-spinner-grey.gif" align=""><span>&nbsp;&nbsp;' + (options.message ? options.message : 'LOADING...') + '</span></div>';
            }

            if (options.target) { // element blocking
                var el = $m(options.target);
                if (el.height() <= ($m(window).height())) {
                    options.cenrerY = true;
                }
                el.block({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 1000,
                    centerY: options.cenrerY !== undefined ? options.cenrerY : false,
                    css: {
                        top: '10%',
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity: options.boxed ? 0.05 : 0.1,
                        cursor: 'wait'
                    }
                });
            } else { // page blocking
                $m.blockUI({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 1000,
                    css: {
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity: options.boxed ? 0.05 : 0.1,
                        cursor: 'wait'
                    }
                });
            }
        },

        // wrApper function to  un-block element(finish loading)
        unblockUI: function(target) {
            if (target) {
                $m(target).unblock({
                    onUnblock: function() {
                        $m(target).css('position', '');
                        $m(target).css('zoom', '');
                    }
                });
            } else {
                $m.unblockUI();
            }
        },

        startPageLoading: function(options) {
            if (options && options.animate) {
                $m('.page-spinner-bar').remove();
                $m('body').append('<div class="page-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>');
            } else {
                $m('.page-loading').remove();
                $m('body').append('<div class="page-loading"><img src="' + this.getGlobalImgPath() + 'loading-spinner-grey.gif"/>&nbsp;&nbsp;<span>' + (options && options.message ? options.message : 'Loading...') + '</span></div>');
            }
        },

        stopPageLoading: function() {
            $m('.page-loading, .page-spinner-bar').remove();
        },

        alert: function(options) {

            options = $m.extend(true, {
                container: "", // alerts parent container(by default placed after the page breadcrumbs)
                place: "append", // "append" or "prepend" in container 
                type: 'success', // alert's type
                message: "", // alert's message
                close: true, // make alert closable
                reset: true, // close all previouse alerts first
                focus: true, // auto scroll to the alert after shown
                closeInSeconds: 0, // auto close after defined seconds
                icon: "" // put icon before the message
            }, options);

            var id = App.getUniqueID("App_alert");

            var html = '<div id="' + id + '" class="custom-alerts alert alert-' + options.type + ' fade in">' + (options.close ? '<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>' : '') + (options.icon !== "" ? '<i class="fa-lg fa fa-' + options.icon + '"></i>  ' : '') + options.message + '</div>';

            if (options.reset) {
                $m('.custom-alerts').remove();
            }

            if (!options.container) {
                if ($m('body').hasClass("page-container-bg-solid") || $m('body').hasClass("page-content-white")) {
                    $m('.page-title').after(html);
                } else {
                    if ($m('.page-bar').size() > 0) {
                        $m('.page-bar').after(html);
                    } else {
                        $m('.page-breadcrumb').after(html);
                    }
                }
            } else {
                if (options.place == "append") {
                    $m(options.container).append(html);
                } else {
                    $m(options.container).prepend(html);
                }
            }

            if (options.focus) {
                App.scrollTo($m('#' + id));
            }

            if (options.closeInSeconds > 0) {
                setTimeout(function() {
                    $m('#' + id).remove();
                }, options.closeInSeconds * 1000);
            }

            return id;
        },

        // initializes uniform elements
        initUniform: function(els) {
            if (els) {
                $m(els).each(function() {
                    if ($m(this).parents(".checker").size() === 0) {
                        $m(this).show();
                        $m(this).uniform();
                    }
                });
            } else {
                handleUniform();
            }
        },

        //wrApper function to update/sync jquery uniform checkbox & radios
        updateUniform: function(els) {
            $m.uniform.update(els); // update the uniform checkbox & radios UI after the actual input control state changed
        },

        //public function to initialize the fancybox plugin
        initFancybox: function() {
            handleFancybox();
        },

        //public helper function to get actual input value(used in IE9 and IE8 due to placeholder attribute not supported)
        getActualVal: function(el) {
            el = $m(el);
            if (el.val() === el.attr("placeholder")) {
                return "";
            }
            return el.val();
        },

        //public function to get a paremeter by name from URL
        getURLParameter: function(paramName) {
            var searchString = window.location.search.substring(1),
                i, val, params = searchString.split("&");

            for (i = 0; i < params.length; i++) {
                val = params[i].split("=");
                if (val[0] == paramName) {
                    return unescape(val[1]);
                }
            }
            return null;
        },

        // check for device touch support
        isTouchDevice: function() {
            try {
                document.createEvent("TouchEvent");
                return true;
            } catch (e) {
                return false;
            }
        },

        // To get the correct viewport width based on  http://andylangton.co.uk/articles/javascript/get-viewport-size-javascript/
        getViewPort: function() {
            var e = window,
                a = 'inner';
            if (!('innerWidth' in window)) {
                a = 'client';
                e = document.documentElement || document.body;
            }

            return {
                width: e[a + 'Width'],
                height: e[a + 'Height']
            };
        },

        getUniqueID: function(prefix) {
            return 'prefix_' + Math.floor(Math.random() * (new Date()).getTime());
        },

        // check IE8 mode
        isIE8: function() {
            return isIE8;
        },

        // check IE9 mode
        isIE9: function() {
            return isIE9;
        },

        //check RTL mode
        isRTL: function() {
            return isRTL;
        },

        // check IE8 mode
        isAngularJsApp: function() {
            return (typeof angular == 'undefined') ? false : true;
        },

        getAssetsPath: function() {
            return assetsPath;
        },

        setAssetsPath: function(path) {
            assetsPath = path;
        },

        setGlobalImgPath: function(path) {
            globalImgPath = path;
        },

        getGlobalImgPath: function() {
            return assetsPath + globalImgPath;
        },

        setGlobalPluginsPath: function(path) {
            globalPluginsPath = path;
        },

        getGlobalPluginsPath: function() {
            return assetsPath + globalPluginsPath;
        },

        getGlobalCssPath: function() {
            return assetsPath + globalCssPath;
        },

        // get layout color code by color name
        getBrandColor: function(name) {
            if (brandColors[name]) {
                return brandColors[name];
            } else {
                return '';
            }
        },

        getResponsiveBreakpoint: function(size) {
            // bootstrap responsive breakpoints
            var sizes = {
                'xs' : 480,     // extra small
                'sm' : 768,     // small
                'md' : 992,     // medium
                'lg' : 1200     // large
            };

            return sizes[size] ? sizes[size] : 0; 
        }
    };

}();

$m = jQuery.noConflict();
$m(document).ready(function() {
    App.init(); // init metronic core componets
});

