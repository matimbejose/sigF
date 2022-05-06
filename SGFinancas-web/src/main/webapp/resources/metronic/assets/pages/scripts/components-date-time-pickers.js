var ComponentsDateTimePickers = function () {

    var handleDatePickers = function () {

        if (jQuery().datepicker) {
            $('.date-picker').datepicker({
                rtl: App.isRTL(),
                orientation: "left",
                autoclose: true
            });
            //$('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
        }

        /* Workaround to restrict daterange past date select: http://stackoverflow.com/questions/11933173/how-to-restrict-the-selectable-date-ranges-in-bootstrap-datepicker */

        // Workaround to fix datepicker position on window scroll
        $(document).scroll(function () {
            $('#form_modal2 .date-picker').datepicker('place'); //#modal is the id of the modal
        });
    }

    var handleTimePickers = function () {

        if (jQuery().timepicker) {
            $('.timepicker-default').timepicker({
                autoclose: true,
                showSeconds: true,
                minuteStep: 1
            });

            $('.timepicker-no-seconds').timepicker({
                autoclose: true,
                minuteStep: 5,
                defaultTime: false
            });

            $('.timepicker-24').timepicker({
                autoclose: true,
                minuteStep: 5,
                showSeconds: false,
                showMeridian: false
            });

            // handle input group button click
            $('.timepicker').parent('.input-group').on('click', '.input-group-btn', function (e) {
                e.preventDefault();
                $(this).parent('.input-group').find('.timepicker').timepicker('showWidget');
            });

            // Workaround to fix timepicker position on window scroll
            $(document).scroll(function () {
                $('#form_modal4 .timepicker-default, #form_modal4 .timepicker-no-seconds, #form_modal4 .timepicker-24').timepicker('place'); //#modal is the id of the modal
            });
        }
    }

    var handleDateRangePickers = function () {
		
        if (!jQuery().daterangepicker) {
            return;
        }
		
        $m('#defaultrange').daterangepicker({
            opens: (App.isRTL() ? 'left' : 'right'),
            format: 'MM/DD/YYYY',
            separator: ' to ',
            startDate: moment().subtract('days', 29),
            endDate: moment(),
            minDate: '01/01/2012',
            maxDate: '12/31/2018',
        },
                function (start, end) {
                    $m('#defaultrange input').val(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                }
        );

        $m('#defaultrange_modal').daterangepicker({
            opens: (App.isRTL() ? 'left' : 'right'),
            format: 'MM/DD/YYYY',
            separator: ' to ',
            startDate: moment().subtract('days', 29),
            endDate: moment(),
            minDate: '01/01/2012',
            maxDate: '12/31/2018',
        },
                function (start, end) {
                    $m('#defaultrange_modal input').val(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                }
        );

        // this is very important fix when daterangepicker is used in modal. in modal when daterange picker is opened and mouse clicked anywhere bootstrap modal removes the modal-open class from the body element.
        // so the below code will fix this issue.
        $m('#defaultrange_modal').on('click', function () {
            if ($m('#daterangepicker_modal').is(":visible") && $('body').hasClass("modal-open") == false) {
                $m('body').addClass("modal-open");
            }
        });

        $m('#reportrange').daterangepicker({
            opens: (App.isRTL() ? 'right' : 'left'),
            startDate: moment().subtract('days', 30),
            endDate: moment(),
            showDropdowns: true,
            showWeekNumbers: true,
            ranges: {
                'Mês atual': [moment().startOf('month'), moment().endOf('month')],
                'Mês passado': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')],
                'Mês seguinte': [moment().add('month', 1).startOf('month'), moment().add('month', 1).endOf('month')],
                'Últimos 6 meses': [moment().subtract('month', 5).startOf('month'), moment().endOf('month')],
                'Próximos 6 meses': [moment(), moment().add('month', 5).endOf('month')],
                'Todos registros': [moment().subtract('year', 200).startOf('year'), moment().add('year', 200).endOf('year')]
            },
            buttonClasses: ['btn btn-sm'],
            applyClass: 'blue',
            cancelClass: 'default',
            format: 'DD/MM/YYYY',
            separator: ' to ',
            locale: {
                format: "DD/MM/YYYY",
                applyLabel: 'Aplicar',
                fromLabel: 'De',
                toLabel: 'Até',
                cancelLabel: 'Cancelar',
                customRangeLabel: 'Período específico',
                daysOfWeek: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
                monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
                firstDay: 1
            }
        },
                function (start, end) {
                    if (parseInt(start.format('YYYY')) > 1980) {
                        $m('#reportrange span').html(start.format('D MMMM YYYY') + ' - ' + end.format('D MMMM YYYY'));
                    } else {
                        $m('#reportrange span').html('Todos registros');
                    }
                    // atualiza os valores da tela
                    $m(".dataIni").val(start.format('DD/MM/YYYY'));
                    $m(".dataFim").val(end.format('DD/MM/YYYY'));

                    rc();
                }
        );

        $m('#reportrangeAnual').daterangepicker({
            opens: (App.isRTL() ? 'right' : 'left'),
            startDate: moment().startOf('year'),
            endDate: moment().endOf('year'),
            showDropdowns: true,
            showWeekNumbers: true,
            ranges: {
                'Ano atual': [moment().startOf('year'), moment().endOf('year')],
                'Ano passado': [moment().startOf('year').subtract('month', 12), moment().endOf('year').subtract('month', 12)],
                'Próximo ano': [moment().startOf('year').add('month', 12), moment().endOf('year').add('month', 12)]
            },
            buttonClasses: ['btn btn-sm'],
            applyClass: 'blue',
            cancelClass: 'default',
            format: 'DD/MM/YYYY',
            separator: ' to ',
            locale: {
                format: "DD/MM/YYYY",
                applyLabel: 'Aplicar',
                fromLabel: 'De',
                toLabel: 'Até',
                cancelLabel: 'Cancelar',
                customRangeLabel: 'Período específico',
                daysOfWeek: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
                monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
                firstDay: 1
            }
        },
                function (start, end) {
                    $m('#reportrangeAnual span').html(start.format('D MMMM YYYY') + ' - ' + end.format('D MMMM YYYY'));
                    // atualiza os valores da tela
                    $m(".anoIni").val(start.format('DD/MM/YYYY'));
                    $m(".anoFim").val(end.format('DD/MM/YYYY'));
                    rc();

                }
        );
		
				//Set the initial state of the picker label
        $m('#reportrange span').html('MÊS ATUAL');
        $m('#reportrangeAnual span').html(moment().startOf('month').format('D MMMM YYYY') + ' - ' + moment().endOf('month').format('D MMMM YYYY'));

        // atualiza os valores da tela
        $m(".dataIni").val(moment().subtract('month', 0).startOf('month').format('DD/MM/YYYY'));
        $m(".dataFim").val(moment().add('month', 0).endOf('month').format('DD/MM/YYYY'));
        
        $m(".anoIni").val(moment().startOf('month').format('DD/MM/YYYY'));
        $m(".anoFim").val(moment().endOf('month').format('DD/MM/YYYY'));
        rc();

    }

    var handleDatetimePicker = function () {

        if (!jQuery().datetimepicker) {
            return;
        }

        $m(".form_datetime").datetimepicker({
            autoclose: true,
            isRTL: App.isRTL(),
            format: "dd MM yyyy - hh:ii",
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left")
        });

        $m(".form_advance_datetime").datetimepicker({
            isRTL: App.isRTL(),
            format: "dd MM yyyy - hh:ii",
            autoclose: true,
            todayBtn: true,
            startDate: "2013-02-14 10:00",
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left"),
            minuteStep: 10
        });

        $m(".form_meridian_datetime").datetimepicker({
            isRTL: App.isRTL(),
            format: "dd MM yyyy - HH:ii P",
            showMeridian: true,
            autoclose: true,
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left"),
            todayBtn: true
        });

        $m('body').removeClass("modal-open"); // fix bug when inline picker is used in modal

        // Workaround to fix datetimepicker position on window scroll
        $m(document).scroll(function () {
            $('#form_modal1 .form_datetime, #form_modal1 .form_advance_datetime, #form_modal1 .form_meridian_datetime').datetimepicker('place'); //#modal is the id of the modal
        });
    }

    var handleClockfaceTimePickers = function () {

        if (!jQuery().clockface) {
            return;
        }

        $m('.clockface_1').clockface();

        $m('#clockface_2').clockface({
            format: 'HH:mm',
            trigger: 'manual'
        });

        $m('#clockface_2_toggle').click(function (e) {
            e.stopPropagation();
            $('#clockface_2').clockface('toggle');
        });

        $m('#clockface_2_modal').clockface({
            format: 'HH:mm',
            trigger: 'manual'
        });

        $m('#clockface_2_modal_toggle').click(function (e) {
            e.stopPropagation();
            $('#clockface_2_modal').clockface('toggle');
        });

        $m('.clockface_3').clockface({
            format: 'H:mm'
        }).clockface('show', '14:30');

        // Workaround to fix clockface position on window scroll
        $m(document).scroll(function () {
            $m('#form_modal5 .clockface_1, #form_modal5 #clockface_2_modal').clockface('place'); //#modal is the id of the modal
        });
    }

    return {
        //main function to initiate the module
        init: function () {
            handleDatePickers();
            handleTimePickers();
            handleDatetimePicker();
            handleDateRangePickers();
            handleClockfaceTimePickers();
        }
    };

}();

if (App.isAngularJsApp() === false) {
    $m(document).ready(function () {
        ComponentsDateTimePickers.init();
    });
}