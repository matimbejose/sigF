// -------------------------------------------------------------------------------------------------
// If you have a license of the Web PKI component, place it below (you need a license to run the
// application outside of localhost)
// -------------------------------------------------------------------------------------------------


// Binary license
//var myLicense = 'Ag4AMjAxLjg2LjExMy4xMzBDAGlwNDoxMC4wLjAuMC84LGlwNDoxMjcuMC4wLjAvOCxpcDQ6MTcyLjE2LjAuMC8xMixpcDQ6MTkyLjE2OC4wLjAvMTYIAFN0YW5kYXJkCAAAwLkCJZDUCAABLm3RtpC2l4ZkWS+8EniQqC7TRX9M0sO3v7msGlg06bsvkEqxNg5Q3Z7yxrrzIR2daO3OfWCi67EJfisyYfpcub8GPt6M/aMbEh5qkFVnM1RIiCY5BnpolQTrex+M2LTJEFjZ4uqHXlChtVaS5CGi4dor8cLSOnlp1W4BvGDt7fNC7Y91iEqYI9/2AaKgKV0DkNC7i0AdCrMAs4iDriW3sG0Jx/tPNEf5HFqszYYkymqqqIkYHYA/GJpWVfLQNAX04okx7Pgd35PkToubIY7TIPi7xLbGOUsnZ0WqNHeEJZe9lajz5uB4HxF/D4Qv94Gkyc2OF3ZbynVAr3jv+jED0A==';
//var myLicense = 'AjAAMjAxLjg2LjExMy4xMzEsc2dmaW5hbmNhcy5jb20sd3d3LnNnZmluYW5jYXMuY29tQwBpcDQ6MTAuMC4wLjAvOCxpcDQ6MTI3LjAuMC4wLzgsaXA0OjE3Mi4xNi4wLjAvMTIsaXA0OjE5Mi4xNjguMC4wLzE2CABTdGFuZGFyZAgAAEDuRJb12QgAATLWHh3XFxBm+FKRKR7MF826VsLLzwerRCo85GjgpQrJtU8KQfN3PQtiWvXx+0JdtPgE9bSL+PQtVBKBY3P0Wu+hG79emixRSrosyZ/GwwmoKbJzfgftV6vB56UzZsxTgzjAbvh5FmaKRET2bGcIuKvB0jEB9BKvsGBBPSR/bS7z3plrr2V373mtYDCs6kpuZCohJGwnVED2yTd3gX76ipEe/ZitI+8klRYCaFL41HrwN11PjgXLPJ4f08gYEwmT59MoBmurOT2x5lcjuJ1TsnY8nsu6VJ20RCwMr8qq4yj0S6fZDU3bda0otL8My2IAf/cvSZAx4M+SvH0uLaApmps=';
var myLicense = 'AoEAMTg5LjExMi41MS4yMjUsYXBwLnNnZmluYW5jYXMuY29tLGFwcC5zZ2ZpbmFuY2FzLmNvbS5icixzZ2ZpbmFuY2FzLmNvbSxzZ2ZpbmFuY2FzLmNvbS5icix3d3cuc2dmaW5hbmNhcy5jb20sd3d3LnNnZmluYW5jYXMuY29tLmJyMABpcDQ6MTAuMC4wLjAvOCxpcDQ6MTI3LjAuMC4wLzgsaXA0OjE3Mi4xNi4wLjAvMTIIAFN0YW5kYXJkCAAA2FJPZQ/bCAABnvLUKd/SmDJieU88Mpq0itcS/s+B9OOycFQj/nuQ9A7FUsAv499ZnCDfp/n/Hn1tEM/Wf/1zztRJZWKNYEAm+NNl6qtjvgslQafCfD3itIq3N2HlNkW2gCPVm93sJmTHQOAB63o0sCbADhhEqM2zaKm8BUlXKhCad08FgMt2N4UWbXHX3OZCTpM7/BQMBDzSeYnBhgzk67ayJfak7/bfZ1RtXLdYp1c9YBLb401n4DAVF5SJ7n+pc8pvhK4iHnA3imEDA3B704PrtxIRzv4NUsJiwzSdUCDwxU43CAxqme1WRZ7kBGE/3qO95UNj8cTE6mt+q1mGH9U7wf9DnBcafQ==';

var webPkiLicense = myLicense;
//                  ^^^^--- Web PKI license goes here
// -------------------------------------------------------------------------------------------------

var batchSignatureForm = (function () {

    var pki = null;
    var formElements = {};
    var startAction = null;
    var signatureCount = 0;
    var selectedCertThumbprint = null;
    var selectedCertContent = null;

	// -------------------------------------------------------------------------------------------------
    // Function called once the page is loaded
    // -------------------------------------------------------------------------------------------------
    var init = function(args) {

        formElements = args.formElements;
        startAction = args.startAction;
        signatureCount = args.signatureCount;

        // Wire-up of button clicks
        formElements.signButton.click(sign);
        formElements.refreshButton.click(refresh);

        // Block the UI while we get things ready
        $.blockUI();

        // Instantiate the "LacunaWebPKI" object
        pki = new LacunaWebPKI(webPkiLicense);

        // Call the init() method on the LacunaWebPKI object, passing a callback for when
        // the component is ready to be used and another to be called when an error occurs
        // on any of the subsequent operations. For more information, see:
        // https://webpki.lacunasoftware.com/#/Documentation#coding-the-first-lines
        // http://webpki.lacunasoftware.com/Help/classes/LacunaWebPKI.html#method_init
        pki.init({
            ready: loadCertificates, // as soon as the component is ready we'll load the certificates
            defaultError: onWebPkiError
        });
    };

    // -------------------------------------------------------------------------------------------------
    // Function called when the user clicks the "Refresh" button
    // -------------------------------------------------------------------------------------------------
    var refresh = function() {
        // Block the UI while we load the certificates
        $.blockUI();
        // Invoke the loading of the certificates
        loadCertificates();
    };

	// -------------------------------------------------------------------------------------------------
	// Function that loads the certificates, either on startup or when the user
	// clicks the "Refresh" button. At this point, the UI is already blocked.
	// -------------------------------------------------------------------------------------------------
    var loadCertificates = function() {

        // Call the listCertificates() method to list the user's certificates
        pki.listCertificates({

            // specify that expired certificates should be ignored
            //filter: pki.filters.isWithinValidity,

            // in order to list only certificates within validity period and having a CPF (ICP-Brasil), use this instead:
            //filter: pki.filters.all(pki.filters.hasPkiBrazilCpf, pki.filters.isWithinValidity),

            // id of the select to be populated with the certificates
            selectId: formElements.certificateSelect.attr('id'),

            // function that will be called to get the text that should be displayed for each option
            selectOptionFormatter: function (cert) {
                return cert.subjectName + ' (expires on ' + cert.validityEnd.toDateString() + ', issued by ' + cert.issuerName + ')';
            }

        }).success(function () {

            // once the certificates have been listed, unblock the UI
            $.unblockUI();

        });
    };

    // -------------------------------------------------------------------------------------------------
    // Function called when the user clicks the "Sign" button
    // -------------------------------------------------------------------------------------------------
    var sign = function() {

        // Block the UI while we perform the signature
        $.blockUI();

        // Get the thumbprint of the selected certificate
        selectedCertThumbprint = formElements.certificateSelect.val();

        // Call the preauthorizeSignatures function to ask authorization for performing all signatures (this ensures
        // that the authorization dialog will appear only once)
        pki.preauthorizeSignatures({
            certificateThumbprint: selectedCertThumbprint,
            signatureCount: signatureCount
        }).success(function () {
            // Read the selected certificate's encoding
            pki.readCertificate(selectedCertThumbprint).success(function (certificate) {
                // Store the certificate's encoding on a local variable
                selectedCertContent = certificate;
                // Call the "startAction" to get the parameters for the first signature
                $.ajax({
                    method: 'POST',
                    url: startAction,
                    dataType: 'json',
                    success: signStep,
                    error: onServerError
                });
            });
        });
    };

    // -------------------------------------------------------------------------------------------------
    // Function called when the parameters for the signature of the next step have been retrieved from
    // the server
    // -------------------------------------------------------------------------------------------------
    var signStep = function (model) {

        // If the "redirectTo" field is filled, the process is complete
        if (model.redirectTo) {
            window.location = model.redirectTo;
            return;
        }

        // Perform the signature using the parameters given by the server
        pki.signHash({
            thumbprint: selectedCertThumbprint,
            hash: model.toSignHash,
            digestAlgorithm: 'SHA-1'
        }).success(function(signature) {
            // Submit the result to the server and get the parameters for the next step
            $.ajax({
                method: 'POST',
                url: model.action,
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({
                    certificate: selectedCertContent,
                    signature: signature
                }),
                dataType: 'json',
                success: signStep,
                error: onServerError
            });
        });
    };

    // -------------------------------------------------------------------------------------------------
    // Function called if an error occurs on the Web PKI component
    // -------------------------------------------------------------------------------------------------
    var onWebPkiError = function(message, error, origin) {
        // Unblock the UI
        $.unblockUI();
        // Log the error to the browser console (for debugging purposes)
        if (console && console.log) {
            console.log('An error has occurred on the signature browser component: ' + message, error);
        }
        // Show the message to the user. You might want to substitute the alert below with a more user-friendly UI
        // component to show the error.
        alert(message);
    };

    // -------------------------------------------------------------------------------------------------
    // Function called if an error occurs on the backend
    // -------------------------------------------------------------------------------------------------
    var onServerError = function(jqXHR, textStatus, errorThrown) {
        // Unblock the UI
        $.unblockUI();
        // Log the error to the browser console (for debugging purposes)
        if (console && console.log) {
            console.log('A server error has occurred', { jqXHR, textStatus, errorThrown });
        }
        // Show the message to the user. You might want to substitute the alert below with a more user-friendly UI
        // component to show the error.
        alert('Server error ' + jqXHR.status + ': ' + errorThrown);
    };

    return {
        init: init
    };

})();