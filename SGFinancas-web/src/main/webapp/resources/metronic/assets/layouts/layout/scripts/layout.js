/**
Core script to handle the entire theme and core functions
**/
var Layout = function () {

    var layoutImgPath = 'layouts/layout/img/';  

    var layoutCssPath = 'layouts/layout/css/';

    var resBreakpointMd = App.getResponsiveBreakpoint('md');

    var ajaxContentSuccessCallbacks = [];
    var ajaxContentErrorCallbacks = [];

    //* BEGIN:CORE HANDLERS *//
    // this function handles responsive layout on screen size resize or mobile device rotate.

    // Set proper height for sidebar and content. The content and sidebar height must be synced always.
    var handleSidebarAndContentHeight = function () {
        var content = $m('.page-content');
        var sidebar = $m('.page-sidebar');
        var body = $m('body');
        var height;

        if (body.hasClass("page-footer-fixed") === true && body.hasClass("page-sidebar-fixed") === false) {
            var available_height = App.getViewPort().height - $m('.page-footer').outerHeight() - $m('.page-header').outerHeight();
            var sidebar_height = sidebar.outerHeight();
            if (sidebar_height > available_height) {
                available_height = sidebar_height + $m('.page-footer').outerHeight();
            }
            if (content.height() < available_height) {
                content.css('min-height', available_height);
            }
        } else {
            if (body.hasClass('page-sidebar-fixed')) {
                height = _calculateFixedSidebarViewportHeight();
                if (body.hasClass('page-footer-fixed') === false) {
                    height = height - $m('.page-footer').outerHeight();
                }
            } else {
                var headerHeight = $m('.page-header').outerHeight();
                var footerHeight = $m('.page-footer').outerHeight();

                if (App.getViewPort().width < resBreakpointMd) {
                    height = App.getViewPort().height - headerHeight - footerHeight;
                } else {
                    height = sidebar.height() + 20;
                }

                if ((height + headerHeight + footerHeight) <= App.getViewPort().height) {
                    height = App.getViewPort().height - headerHeight - footerHeight;
                }
            }
            content.css('min-height', height);
        }
    };

    // Handle sidebar menu links
    var handleSidebarMenuActiveLink = function (mode, el, $state) {
        var url = location.hash.toLowerCase();
        var menu = $m('.page-sidebar-menu');
 
        if (mode === 'click' || mode === 'set') {
            el = $m(el);
        } else if (mode === 'match') {
            menu.find('li > a').each(function () {
                var state = $m(this).attr('ui-sref');
                if ($state && state) {
                    if ($state.is(state)) {
                        el = $m(this);
                        return;
                    }
                } else {
                    var path = $m(this).attr('href');
                    if (path) {
                        // url match condition         
                        path = path.toLowerCase();
                        if (path.length > 1 && url.substr(1, path.length - 1) == path.substr(1)) {
                            el = $m(this);
                            return;
                        }
                    }
                }
            });
        }
 
        if (!el || el.size() == 0) {
            return;
        }
 
        if (el.attr('href') == 'javascript:;' ||
            el.attr('ui-sref') == 'javascript:;' ||
            el.attr('href') == '#' ||
            el.attr('ui-sref') == '#'
            ) {
            return;
        }
 
        var slideSpeed = parseInt(menu.data('slide-speed'));
        var keepExpand = menu.data('keep-expanded');
 
        // begin: handle active state
        if (menu.hasClass('page-sidebar-menu-hover-submenu') === false) {
            menu.find('li.nav-item.open').each(function () {
                var match = false;
                $m(this).find('li').each(function () {
                    var state = $m(this).attr('ui-sref');
                    if ($state && state) {
                        if ($state.is(state)) {
                            match = true;
                            return;
                        }
                    } else if ($m(this).find(' > a').attr('href') === el.attr('href')) {
                        match = true;
                        return;
                    }
                });
 
                if (match === true) {
                    return;
                }
 
                $m(this).removeClass('open');
                $m(this).find('> a > .arrow.open').removeClass('open');
                $m(this).find('> .sub-menu').slideUp();
            });
        } else {
            menu.find('li.open').removeClass('open');
        }
 
        menu.find('li.active').removeClass('active');
        menu.find('li > a > .selected').remove();
        // end: handle active state
 
        el.parents('li').each(function () {
            $m(this).addClass('active');
            $m(this).find('> a > span.arrow').addClass('open');
 
            if ($m(this).parent('ul.page-sidebar-menu').size() === 1) {
                $m(this).find('> a').append('<span class="selected"></span>');
            }
 
            if ($m(this).children('ul.sub-menu').size() === 1) {
                $m(this).addClass('open');
            }
        });
 
        if (mode === 'click') {
            if (App.getViewPort().width < resBreakpointMd && $m('.page-sidebar').hasClass('in')) { // close the menu on mobile view while laoding a page 
                $m('.page-header .responsive-toggler').click();
            }
        }
    };

    // Handle sidebar menu
    var handleSidebarMenu = function () {
        // offcanvas mobile menu 
        $m('.page-sidebar-mobile-offcanvas .responsive-toggler').click(function(e) {
            $m('body').toggleClass('page-sidebar-mobile-offcanvas-open');
            e.preventDefault();
            e.stopPropagation();
        });

        if ($m('body').hasClass('page-sidebar-mobile-offcanvas')) {
            $m(document).on('click', function(e) {
                if ($m('body').hasClass('page-sidebar-mobile-offcanvas-open')) {
                    if ($m(e.target).closest('.page-sidebar-mobile-offcanvas .responsive-toggler').length === 0 && 
                        $m(e.target).closest('.page-sidebar-wrapper').length === 0) { 
                        $m('body').removeClass('page-sidebar-mobile-offcanvas-open');
                        e.preventDefault();
                        e.stopPropagation();
                    }
                }                
            });
        }

        // handle sidebar link click
        $m('.page-sidebar-menu').on('click', 'li > a.nav-toggle, li > a > span.nav-toggle', function (e) {
            var that = $m(this).closest('.nav-item').children('.nav-link');

            if (App.getViewPort().width >= resBreakpointMd && !$m('.page-sidebar-menu').attr("data-initialized") && $m('body').hasClass('page-sidebar-closed') &&  that.parent('li').parent('.page-sidebar-menu').size() === 1) {
                return;
            }

            var hasSubMenu = that.next().hasClass('sub-menu');

            if (App.getViewPort().width >= resBreakpointMd && that.parents('.page-sidebar-menu-hover-submenu').size() === 1) { // exit of hover sidebar menu
                return;
            }

            if (hasSubMenu === false) {
                if (App.getViewPort().width < resBreakpointMd && $m('.page-sidebar').hasClass("in")) { // close the menu on mobile view while laoding a page 
                    $m('.page-header .responsive-toggler').click();
                }
                return;
            }

            var parent =that.parent().parent();
            var the = that;
            var menu = $m('.page-sidebar-menu');
            var sub = that.next();

            var autoScroll = menu.data("auto-scroll");
            var slideSpeed = parseInt(menu.data("slide-speed"));
            var keepExpand = menu.data("keep-expanded");
            
            if (!keepExpand) {
                parent.children('li.open').children('a').children('.arrow').removeClass('open');
                parent.children('li.open').children('.sub-menu:not(.always-open)').slideUp(slideSpeed);
                parent.children('li.open').removeClass('open');
            }

            var slideOffeset = -200;

            if (sub.is(":visible")) {
                $m('.arrow', the).removeClass("open");
                the.parent().removeClass("open");
                sub.slideUp(slideSpeed, function () {
                    if (autoScroll === true && $m('body').hasClass('page-sidebar-closed') === false) {
                        if ($m('body').hasClass('page-sidebar-fixed')) {
                            menu.slimScroll({
                                'scrollTo': (the.position()).top
                            });
                        } else {
                            App.scrollTo(the, slideOffeset);
                        }
                    }
                    handleSidebarAndContentHeight();
                });
            } else if (hasSubMenu) {
                $m('.arrow', the).addClass("open");
                the.parent().addClass("open");
                sub.slideDown(slideSpeed, function () {
                    if (autoScroll === true && $m('body').hasClass('page-sidebar-closed') === false) {
                        if ($m('body').hasClass('page-sidebar-fixed')) {
                            menu.slimScroll({
                                'scrollTo': (the.position()).top
                            });
                        } else {
                            App.scrollTo(the, slideOffeset);
                        }
                    }
                    handleSidebarAndContentHeight();
                });
            }

            e.preventDefault();
        });

        // handle menu close for angularjs version
        if (App.isAngularJsApp()) {
            $m(".page-sidebar-menu li > a").on("click", function(e) {
                if (App.getViewPort().width < resBreakpointMd && $m(this).next().hasClass('sub-menu') === false) {
                    $m('.page-header .responsive-toggler').click();
                }
            });
        }

        // handle ajax links within sidebar menu
        $m('.page-sidebar').on('click', ' li > a.ajaxify', function (e) {
            e.preventDefault();
            App.scrollTop();
            var url = $m(this).attr("href");
            var menuContainer = $m('.page-sidebar ul');

            menuContainer.children('li.active').removeClass('active');
            menuContainer.children('arrow.open').removeClass('open');

            $m(this).parents('li').each(function () {
                $m(this).addClass('active');
                $m(this).children('a > span.arrow').addClass('open');
            });
            $m(this).parents('li').addClass('active');

            if (App.getViewPort().width < resBreakpointMd && $m('.page-sidebar').hasClass("in")) { // close the menu on mobile view while laoding a page 
                $m('.page-header .responsive-toggler').click();
            }

            Layout.loadAjaxContent(url, $m(this));
        });

        // handle ajax link within main content
        $m('.page-content').on('click', '.ajaxify', function (e) {
            e.preventDefault();
            App.scrollTop();

            var url = $m(this).attr("href");

            if (App.getViewPort().width < resBreakpointMd && $m('.page-sidebar').hasClass("in")) { // close the menu on mobile view while laoding a page 
                $m('.page-header .responsive-toggler').click();
            }

            Layout.loadAjaxContent(url);
        });

        // handle scrolling to top on responsive menu toggler click when header is fixed for mobile view
        $m(document).on('click', '.page-header-fixed-mobile .page-header .responsive-toggler', function(){
            App.scrollTop(); 
        });      
     
        // handle sidebar hover effect        
        handleFixedSidebarHoverEffect();

        // handle the search bar close
        $m('.page-sidebar').on('click', '.sidebar-search .remove', function (e) {
            e.preventDefault();
            $m('.sidebar-search').removeClass("open");
        });

        // handle the search query submit on enter press
        $m('.page-sidebar .sidebar-search').on('keypress', 'input.form-control', function (e) {
            if (e.which == 13) {
                $m('.sidebar-search').submit();
                return false; //<---- Add this line
            }
        });

        // handle the search submit(for sidebar search and responsive mode of the header search)
        $m('.sidebar-search .submit').on('click', function (e) {
            e.preventDefault();
            if ($m('body').hasClass("page-sidebar-closed")) {
                if ($m('.sidebar-search').hasClass('open') === false) {
                    if ($m('.page-sidebar-fixed').size() === 1) {
                        $m('.page-sidebar .sidebar-toggler').click(); //trigger sidebar toggle button
                    }
                    $m('.sidebar-search').addClass("open");
                } else {
                    $m('.sidebar-search').submit();
                }
            } else {
                $m('.sidebar-search').submit();
            }
        });

        // handle close on body click
        if ($m('.sidebar-search').size() !== 0) {
            $m('.sidebar-search .input-group').on('click', function(e){
                e.stopPropagation();
            });

            $m('body').on('click', function() {
                if ($m('.sidebar-search').hasClass('open')) {
                    $m('.sidebar-search').removeClass("open");
                }
            });
        }
    };

    // Helper function to calculate sidebar height for fixed sidebar layout.
    var _calculateFixedSidebarViewportHeight = function () {
        var sidebarHeight = App.getViewPort().height - $m('.page-header').outerHeight(true);
        if ($m('body').hasClass("page-footer-fixed")) {
            sidebarHeight = sidebarHeight - $m('.page-footer').outerHeight();
        }

        return sidebarHeight;
    };

    // Handles fixed sidebar
    var handleFixedSidebar = function () {
        var menu = $m('.page-sidebar-menu');

        handleSidebarAndContentHeight();

        if ($m('.page-sidebar-fixed').size() === 0) {
            App.destroySlimScroll(menu);
            return;
        }

        if (App.getViewPort().width >= resBreakpointMd && !$m('body').hasClass('page-sidebar-menu-not-fixed')) {
            menu.attr("data-height", _calculateFixedSidebarViewportHeight());
            App.destroySlimScroll(menu);
            App.initSlimScroll(menu);
            handleSidebarAndContentHeight();
        } 
    };

    // Handles sidebar toggler to close/hide the sidebar.
    var handleFixedSidebarHoverEffect = function () {
        if ($m('body').hasClass('page-sidebar-fixed')) {
            $m('.page-sidebar').on('mouseenter', function () {
                if ($m('body').hasClass('page-sidebar-closed')) {
                    $m(this).find('.page-sidebar-menu').removeClass('page-sidebar-menu-closed');
                }
            }).on('mouseleave', function () {
                if ($m('body').hasClass('page-sidebar-closed')) {
                    $m(this).find('.page-sidebar-menu').addClass('page-sidebar-menu-closed');
                }
            });
        }
    };

    // Hanles sidebar toggler
    var handleSidebarToggler = function () {       
        /**
        if (Cookies && Cookies.get('sidebar_closed') === '1' && App.getViewPort().width >= resBreakpointMd) {
            $m('body').addClass('page-sidebar-closed');
            $m('.page-sidebar-menu').addClass('page-sidebar-menu-closed');
        }
        */

        // handle sidebar show/hide
        $m('body').on('click', '.sidebar-toggler', function (e) {
            var body = $m('body');
            var sidebar = $m('.page-sidebar');
            var sidebarMenu = $m('.page-sidebar-menu');
            $m(".sidebar-search", sidebar).removeClass("open");

            if (body.hasClass("page-sidebar-closed")) {
                body.removeClass("page-sidebar-closed");
                sidebarMenu.removeClass("page-sidebar-menu-closed");
                if (Cookies) {
                    Cookies.set('sidebar_closed', '0');
                }
            } else {
                body.addClass("page-sidebar-closed");
                sidebarMenu.addClass("page-sidebar-menu-closed");
                if (body.hasClass("page-sidebar-fixed")) {
                    sidebarMenu.trigger("mouseleave");
                }
                if (Cookies) {
                    Cookies.set('sidebar_closed', '1');
                }
            }

            $m(window).trigger('resize');
        });
    };

    // Handles the horizontal menu
    var handleHorizontalMenu = function () {
        //handle tab click
        $m('.page-header').on('click', '.hor-menu a[data-toggle="tab"]', function (e) {
            e.preventDefault();
            var nav = $m(".hor-menu .nav");
            var active_link = nav.find('li.current');
            $m('li.active', active_link).removeClass("active");
            $m('.selected', active_link).remove();
            var new_link = $m(this).parents('li').last();
            new_link.addClass("current");
            new_link.find("a:first").append('<span class="selected"></span>');
        });

        // handle search box expand/collapse        
        $m('.page-header').on('click', '.search-form', function (e) {
            $m(this).addClass("open");
            $m(this).find('.form-control').focus();

            $m('.page-header .search-form .form-control').on('blur', function (e) {
                $m(this).closest('.search-form').removeClass("open");
                $m(this).unbind("blur");
            });
        });

        // handle hor menu search form on enter press
        $m('.page-header').on('keypress', '.hor-menu .search-form .form-control', function (e) {
            if (e.which == 13) {
                $m(this).closest('.search-form').submit();
                return false;
            }
        });

        // handle header search button click
        $m('.page-header').on('mousedown', '.search-form.open .submit', function (e) {
            e.preventDefault();
            e.stopPropagation();
            $m(this).closest('.search-form').submit();
        });

        
        $m(document).on('click', '.mega-menu-dropdown .dropdown-menu', function (e) {
            e.stopPropagation();
        });
    };

    // Handles Bootstrap Tabs.
    var handleTabs = function () {
        // fix content height on tab click
        $m('body').on('shown.bs.tab', 'a[data-toggle="tab"]', function () {
            handleSidebarAndContentHeight();
        });
    };

    // Handles the go to top button at the footer
    var handleGoTop = function () {
        var offset = 300;
        var duration = 500;

        if (navigator.userAgent.match(/iPhone|iPad|iPod/i)) {  // ios supported
            $m(window).bind("touchend touchcancel touchleave", function(e){
               if ($m(this).scrollTop() > offset) {
                    $m('.scroll-to-top').fadeIn(duration);
                } else {
                    $m('.scroll-to-top').fadeOut(duration);
                }
            });
        } else {  // general 
            $m(window).scroll(function() {
                if ($m(this).scrollTop() > offset) {
                    $m('.scroll-to-top').fadeIn(duration);
                } else {
                    $m('.scroll-to-top').fadeOut(duration);
                }
            });
        }
        
        $m('.scroll-to-top').click(function(e) {
            e.preventDefault();
            $m('html, body').animate({scrollTop: 0}, duration);
            return false;
        });
    };

    // Hanlde 100% height elements(block, portlet, etc)
    var handle100HeightContent = function () {

        $m('.full-height-content').each(function(){
            var target = $m(this);
            var height;

            height = App.getViewPort().height -
                $m('.page-header').outerHeight(true) -
                $m('.page-footer').outerHeight(true) -
                $m('.page-title').outerHeight(true) -
                $m('.page-bar').outerHeight(true);

            if (target.hasClass('portlet')) {
                var portletBody = target.find('.portlet-body');

                App.destroySlimScroll(portletBody.find('.full-height-content-body')); // destroy slimscroll 
                
                height = height -
                    target.find('.portlet-title').outerHeight(true) -
                    parseInt(target.find('.portlet-body').css('padding-top')) -
                    parseInt(target.find('.portlet-body').css('padding-bottom')) - 5;

                if (App.getViewPort().width >= resBreakpointMd && target.hasClass("full-height-content-scrollable")) {
                    height = height - 35;
                    portletBody.find('.full-height-content-body').css('height', height);
                    App.initSlimScroll(portletBody.find('.full-height-content-body'));
                } else {
                    portletBody.css('min-height', height);
                }
            } else {
               App.destroySlimScroll(target.find('.full-height-content-body')); // destroy slimscroll 

                if (App.getViewPort().width >= resBreakpointMd && target.hasClass("full-height-content-scrollable")) {
                    height = height - 35;
                    target.find('.full-height-content-body').css('height', height);
                    App.initSlimScroll(target.find('.full-height-content-body'));
                } else {
                    target.css('min-height', height);
                }
            }
        });        
    };
    //* END:CORE HANDLERS *//

    return {
        // Main init methods to initialize the layout
        //IMPORTANT!!!: Do not modify the core handlers call order.

        initHeader: function() {
            handleHorizontalMenu(); // handles horizontal menu    
        },

        setSidebarMenuActiveLink: function(mode, el) {
            handleSidebarMenuActiveLink(mode, el, null);
        },

        setAngularJsSidebarMenuActiveLink: function(mode, el, $state) {
            handleSidebarMenuActiveLink(mode, el, $state);
        },

        initSidebar: function($state) {
            //layout handlers
            handleFixedSidebar(); // handles fixed sidebar menu
            handleSidebarMenu(); // handles main menu
            handleSidebarToggler(); // handles sidebar hide/show

            if (App.isAngularJsApp()) {      
                handleSidebarMenuActiveLink('match', null, $state); // init sidebar active links 
            }

            App.addResizeHandler(handleFixedSidebar); // reinitialize fixed sidebar on window resize
        },

        initContent: function() {
            handle100HeightContent(); // handles 100% height elements(block, portlet, etc)
            handleTabs(); // handle bootstrah tabs

            App.addResizeHandler(handleSidebarAndContentHeight); // recalculate sidebar & content height on window resize
            App.addResizeHandler(handle100HeightContent); // reinitialize content height on window resize 
        },

        initFooter: function() {
            handleGoTop(); //handles scroll to top functionality in the footer
        },

        init: function () {            
            this.initHeader();
            this.initSidebar(null);
            this.initContent();
            this.initFooter();
        },

        loadAjaxContent: function(url, sidebarMenuLink) {
            var pageContent = $m('.page-content .page-content-body');    

            App.startPageLoading({animate: true});
            
            $.ajax({
                type: "GET",
                cache: false,
                url: url,
                dataType: "html",
                success: function (res) {    
                    App.stopPageLoading();
                    pageContent.html(res);

                    for (var i = 0; i < ajaxContentSuccessCallbacks.length; i++) {
                        ajaxContentSuccessCallbacks[i].call(res);
                    }

                    if (sidebarMenuLink.size() > 0 && sidebarMenuLink.parents('li.open').size() === 0) {
                        $m('.page-sidebar-menu > li.open > a').click();
                    }
                    
                    Layout.fixContentHeight(); // fix content height
                    App.initAjax(); // initialize core stuff
                },
                error: function (res, ajaxOptions, thrownError) {
                    App.stopPageLoading();
                    pageContent.html('<h4>Could not load the requested content.</h4>');

                    for (var i = 0; i < ajaxContentErrorCallbacks.length; i++) {
                        ajaxContentErrorCallbacks[i].call(res);
                    }                    
                }
            });
        },

        addAjaxContentSuccessCallback: function(callback) {
            ajaxContentSuccessCallbacks.push(callback);
        },

        addAjaxContentErrorCallback: function(callback) {
            ajaxContentErrorCallbacks.push(callback);
        },

        //public function to fix the sidebar and content height accordingly
        fixContentHeight: function () {
            handleSidebarAndContentHeight();
        },

        initFixedSidebarHoverEffect: function() {
            handleFixedSidebarHoverEffect();
        },

        initFixedSidebar: function() {
            handleFixedSidebar();
        },

        getLayoutImgPath: function () {
            return App.getAssetsPath() + layoutImgPath;
        },

        getLayoutCssPath: function () {
            return App.getAssetsPath() + layoutCssPath;
        }
    };

}();

if (App.isAngularJsApp() === false) {
    jQuery(document).ready(function() {    
       Layout.init(); // init metronic core componets
    });
}