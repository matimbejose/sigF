/**
 Demo script to handle the theme demo
 **/
var Demo = function () {

    // Handle Theme Settings
    var handleTheme = function () {

        var panel = $m('.theme-panel');

        if ($m('body').hasClass('page-boxed') === false) {
            $m('.layout-option', panel).val("fluid");
        }

        $m('.sidebar-option', panel).val("default");
        $m('.page-header-option', panel).val("fixed");
        $m('.page-footer-option', panel).val("default");
        if ($m('.sidebar-pos-option').attr("disabled") === false) {
            $m('.sidebar-pos-option', panel).val(App.isRTL() ? 'right' : 'left');
        }

        //handle theme layout
        var resetLayout = function () {
            $m("body").
                    removeClass("page-boxed").
                    removeClass("page-footer-fixed").
                    removeClass("page-sidebar-fixed").
                    removeClass("page-header-fixed").
                    removeClass("page-sidebar-reversed");

            $m('.page-header > .page-header-inner').removeClass("container");

            if ($m('.page-container').parent(".container").size() === 1) {
                $m('.page-container').insertAfter('body > .clearfix');
            }

            if ($m('.page-footer > .container').size() === 1) {
                $m('.page-footer').html($m('.page-footer > .container').html());
            } else if ($m('.page-footer').parent(".container").size() === 1) {
                $m('.page-footer').insertAfter('.page-container');
                $m('.scroll-to-top').insertAfter('.page-footer');
            }

            $m(".top-menu > .navbar-nav > li.dropdown").removeClass("dropdown-dark");

            $m('body > .container').remove();
        };

        var lastSelectedLayout = '';

        var setLayout = function () {

            var layoutOption = $m('.layout-option', panel).val();
            var sidebarOption = $m('.sidebar-option', panel).val();
            var headerOption = $m('.page-header-option', panel).val();
            var footerOption = $m('.page-footer-option', panel).val();
            var sidebarPosOption = $m('.sidebar-pos-option', panel).val();
            var sidebarStyleOption = $m('.sidebar-style-option', panel).val();
            var sidebarMenuOption = $m('.sidebar-menu-option', panel).val();
            var headerTopDropdownStyle = $m('.page-header-top-dropdown-style-option', panel).val();

            if (sidebarOption == "fixed" && headerOption == "default") {
                alert('Default Header with Fixed Sidebar option is not supported. Proceed with Fixed Header with Fixed Sidebar.');
                $m('.page-header-option', panel).val("fixed");
                $m('.sidebar-option', panel).val("fixed");
                sidebarOption = 'fixed';
                headerOption = 'fixed';
            }

            resetLayout(); // reset layout to default state

            if (layoutOption === "boxed") {
                $m("body").addClass("page-boxed");

                // set header
                $m('.page-header > .page-header-inner').addClass("container");
                var cont = $m('body > .page-wrapper > .clearfix').after('<div class="container"></div>');

                // set content
                $m('.page-container').appendTo('body > .page-wrapper > .container');

                // set footer
                if (footerOption === 'fixed') {
                    $m('.page-footer').html('<div class="container">' + $m('.page-footer').html() + '</div>');
                } else {
                    $m('.page-footer').appendTo('body > .page-wrapper > .container');
                }
            }

            if (lastSelectedLayout != layoutOption) {
                //layout changed, run responsive handler: 
                App.runResizeHandlers();
            }
            lastSelectedLayout = layoutOption;

            //header
            if (headerOption === 'fixed') {
                $m("body").addClass("page-header-fixed");
                $m(".page-header").removeClass("navbar-static-top").addClass("navbar-fixed-top");
            } else {
                $m("body").removeClass("page-header-fixed");
                $m(".page-header").removeClass("navbar-fixed-top").addClass("navbar-static-top");
            }

            //sidebar
            if ($m('body').hasClass('page-full-width') === false) {
                if (sidebarOption === 'fixed') {
                    $m("body").addClass("page-sidebar-fixed");
                    $m("page-sidebar-menu").addClass("page-sidebar-menu-fixed");
                    $m("page-sidebar-menu").removeClass("page-sidebar-menu-default");
                    Layout.initFixedSidebarHoverEffect();
                } else {
                    $m("body").removeClass("page-sidebar-fixed");
                    $m("page-sidebar-menu").addClass("page-sidebar-menu-default");
                    $m("page-sidebar-menu").removeClass("page-sidebar-menu-fixed");
                    $m('.page-sidebar-menu').unbind('mouseenter').unbind('mouseleave');
                }
            }

            // top dropdown style
            if (headerTopDropdownStyle === 'dark') {
                $m(".top-menu > .navbar-nav > li.dropdown").addClass("dropdown-dark");
            } else {
                $m(".top-menu > .navbar-nav > li.dropdown").removeClass("dropdown-dark");
            }

            //footer 
            if (footerOption === 'fixed') {
                $m("body").addClass("page-footer-fixed");
            } else {
                $m("body").removeClass("page-footer-fixed");
            }

            //sidebar style
            if (sidebarStyleOption === 'light') {
                $m(".page-sidebar-menu").addClass("page-sidebar-menu-light");
            } else {
                $m(".page-sidebar-menu").removeClass("page-sidebar-menu-light");
            }

            //sidebar menu 
            if (sidebarMenuOption === 'hover') {
                if (sidebarOption == 'fixed') {
                    $m('.sidebar-menu-option', panel).val("accordion");
                    alert("Hover Sidebar Menu is not compatible with Fixed Sidebar Mode. Select Default Sidebar Mode Instead.");
                } else {
                    $m(".page-sidebar-menu").addClass("page-sidebar-menu-hover-submenu");
                }
            } else {
                $m(".page-sidebar-menu").removeClass("page-sidebar-menu-hover-submenu");
            }

            //sidebar position
            if (App.isRTL()) {
                if (sidebarPosOption === 'left') {
                    $m("body").addClass("page-sidebar-reversed");
                    $m('#frontend-link').tooltip('destroy').tooltip({
                        placement: 'right'
                    });
                } else {
                    $m("body").removeClass("page-sidebar-reversed");
                    $m('#frontend-link').tooltip('destroy').tooltip({
                        placement: 'left'
                    });
                }
            } else {
                if (sidebarPosOption === 'right') {
                    $m("body").addClass("page-sidebar-reversed");
                    $m('#frontend-link').tooltip('destroy').tooltip({
                        placement: 'left'
                    });
                } else {
                    $m("body").removeClass("page-sidebar-reversed");
                    $m('#frontend-link').tooltip('destroy').tooltip({
                        placement: 'right'
                    });
                }
            }

            Layout.fixContentHeight(); // fix content height            
            Layout.initFixedSidebar(); // reinitialize fixed sidebar
        };

        // handle theme colors
        var setColor = function (color) {
            var color_ = (App.isRTL() ? color + '-rtl' : color);
            $m('#style_color').attr("href", Layout.getLayoutCssPath() + 'themes/' + color_ + ".min.css");
            if (color == 'light2') {
                $m('.page-logo img').attr('src', '/resources/images/logo_SG_Financas_menor_invertido.png');
                $m('#spantexto #texto').attr('class', 'caption-subject font-dark sbold uppercase');
            } else {
                $m('.page-logo img').attr('src', '/resources/images/logo_SG_Financas_menor.png');
                $m('#spantexto #texto').attr('class', 'branco caption-subject sbold uppercase');
            }
        };

        var salvarCor = function (color) {
            salvarCorUsuario([{name: 'cor', value: color}]);
        };

        $m('.toggler', panel).click(function () {
            $m('.toggler').hide();
            $m('.toggler-close').show();
            $m('.theme-panel > .theme-options').show();
        });

        $m('.toggler-close', panel).click(function () {
            $m('.toggler').show();
            $m('.toggler-close').hide();
            $m('.theme-panel > .theme-options').hide();
        });

        $m('.theme-colors > ul > li', panel).click(function () {
            var color = $m(this).attr("data-style");
            setColor(color);
            salvarCor(color);
            $m('ul > li', panel).removeClass("current");
            $m(this).addClass("current");
        });

        // set default theme options:

        if ($m("body").hasClass("page-boxed")) {
            $m('.layout-option', panel).val("boxed");
        }

        if ($m("body").hasClass("page-sidebar-fixed")) {
            $m('.sidebar-option', panel).val("fixed");
        }

        if ($m("body").hasClass("page-header-fixed")) {
            $m('.page-header-option', panel).val("fixed");
        }

        if ($m("body").hasClass("page-footer-fixed")) {
            $m('.page-footer-option', panel).val("fixed");
        }

        if ($m("body").hasClass("page-sidebar-reversed")) {
            $m('.sidebar-pos-option', panel).val("right");
        }

        if ($m(".page-sidebar-menu").hasClass("page-sidebar-menu-light")) {
            $m('.sidebar-style-option', panel).val("light");
        }

        if ($m(".page-sidebar-menu").hasClass("page-sidebar-menu-hover-submenu")) {
            $m('.sidebar-menu-option', panel).val("hover");
        }

        var sidebarOption = $m('.sidebar-option', panel).val();
        var headerOption = $m('.page-header-option', panel).val();
        var footerOption = $m('.page-footer-option', panel).val();
        var sidebarPosOption = $m('.sidebar-pos-option', panel).val();
        var sidebarStyleOption = $m('.sidebar-style-option', panel).val();
        var sidebarMenuOption = $m('.sidebar-menu-option', panel).val();

        $m('.layout-option, .page-header-option, .page-header-top-dropdown-style-option, .sidebar-option, .page-footer-option, .sidebar-pos-option, .sidebar-style-option, .sidebar-menu-option', panel).change(setLayout);
    };

    // handle theme style
    var setThemeStyle = function (style) {
        var file = (style === 'rounded' ? 'components-rounded' : 'components');
        file = (App.isRTL() ? file + '-rtl' : file);

        $m('#style_components').attr("href", App.getGlobalCssPath() + file + ".min.css");

        if (typeof Cookies !== "undefined") {
            Cookies.set('layout-style-option', style);
        }
    };

    return {
        //main function to initiate the theme
        init: function () {
            // handles style customer tool
            handleTheme();

            // handle layout style change
            $m('.theme-panel .layout-style-option').change(function () {
                setThemeStyle($m(this).val());
            });

            // set layout style from cookie
            if (typeof Cookies !== "undefined" && Cookies.get('layout-style-option') === 'rounded') {
                setThemeStyle(Cookies.get('layout-style-option'));
                $m('.theme-panel .layout-style-option').val(Cookies.get('layout-style-option'));
            }
        }
    };

}();

if (App.isAngularJsApp() === false) {
    jQuery(document).ready(function () {
        Demo.init(); // init metronic core componets
    });
}