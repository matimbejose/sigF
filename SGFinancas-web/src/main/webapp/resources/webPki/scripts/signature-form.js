// -------------------------------------------------------------------------------------------------
// If you have a license of the Web PKI component, place it below (you need a license to run the
// application outside of localhost)
// -------------------------------------------------------------------------------------------------


// Binary license
//var myLicense = 'Ag4AMjAxLjg2LjExMy4xMzBDAGlwNDoxMC4wLjAuMC84LGlwNDoxMjcuMC4wLjAvOCxpcDQ6MTcyLjE2LjAuMC8xMixpcDQ6MTkyLjE2OC4wLjAvMTYIAFN0YW5kYXJkCAAAwLkCJZDUCAABLm3RtpC2l4ZkWS+8EniQqC7TRX9M0sO3v7msGlg06bsvkEqxNg5Q3Z7yxrrzIR2daO3OfWCi67EJfisyYfpcub8GPt6M/aMbEh5qkFVnM1RIiCY5BnpolQTrex+M2LTJEFjZ4uqHXlChtVaS5CGi4dor8cLSOnlp1W4BvGDt7fNC7Y91iEqYI9/2AaKgKV0DkNC7i0AdCrMAs4iDriW3sG0Jx/tPNEf5HFqszYYkymqqqIkYHYA/GJpWVfLQNAX04okx7Pgd35PkToubIY7TIPi7xLbGOUsnZ0WqNHeEJZe9lajz5uB4HxF/D4Qv94Gkyc2OF3ZbynVAr3jv+jED0A==';
//var myLicense = 'AoEAMjAxLjg2LjExMy4xMzEsYXBwLnNnZmluYW5jYXMuY29tLGFwcC5zZ2ZpbmFuY2FzLmNvbS5icixzZ2ZpbmFuY2FzLmNvbSxzZ2ZpbmFuY2FzLmNvbS5icix3d3cuc2dmaW5hbmNhcy5jb20sd3d3LnNnZmluYW5jYXMuY29tLmJyMABpcDQ6MTAuMC4wLjAvOCxpcDQ6MTI3LjAuMC4wLzgsaXA0OjE3Mi4xNi4wLjAvMTIIAFN0YW5kYXJkCAAAmAbVSvXZCAABiRp1CaXdJVOB6Ip6DUu5jRCgz/+UOnElxBlHaCu7/HmHa+l+WWd9kBz5dF87orXRhCO3UlS1A6Cb7IEv1sGqpjofSopoIUqP/gePjjtGpsM2ErfvlCp4vUe9gvfIY8BSO+qUlGIiEc/3xUnXzzFsqKQkH0NI9lGA5egu0OBuvUguJgFKUKsqf3bRgOdAxAqt0mdNhfT4OjJWtRCLg5nzcd+d9j6LGH0sd9fKlE1v6gnf4nlpDLmdqpwG1aumm/R58OczAthUiuISkVMcwvHsUZ98BYWtLQ7eQgDsiMcEjAmHep0MHujfJu98Us/CIoIy9DMmGMNZTSezXhX5ZGj77g==';
var myLicense = 'AoEAMTg5LjExMi41MS4yMjUsYXBwLnNnZmluYW5jYXMuY29tLGFwcC5zZ2ZpbmFuY2FzLmNvbS5icixzZ2ZpbmFuY2FzLmNvbSxzZ2ZpbmFuY2FzLmNvbS5icix3d3cuc2dmaW5hbmNhcy5jb20sd3d3LnNnZmluYW5jYXMuY29tLmJyMABpcDQ6MTAuMC4wLjAvOCxpcDQ6MTI3LjAuMC4wLzgsaXA0OjE3Mi4xNi4wLjAvMTIIAFN0YW5kYXJkCAAA2FJPZQ/bCAABnvLUKd/SmDJieU88Mpq0itcS/s+B9OOycFQj/nuQ9A7FUsAv499ZnCDfp/n/Hn1tEM/Wf/1zztRJZWKNYEAm+NNl6qtjvgslQafCfD3itIq3N2HlNkW2gCPVm93sJmTHQOAB63o0sCbADhhEqM2zaKm8BUlXKhCad08FgMt2N4UWbXHX3OZCTpM7/BQMBDzSeYnBhgzk67ayJfak7/bfZ1RtXLdYp1c9YBLb401n4DAVF5SJ7n+pc8pvhK4iHnA3imEDA3B704PrtxIRzv4NUsJiwzSdUCDwxU43CAxqme1WRZ7kBGE/3qO95UNj8cTE6mt+q1mGH9U7wf9DnBcafQ==';

var webPkiLicense = myLicense;
//                  ^^^^--- Web PKI license goes here
// -------------------------------------------------------------------------------------------------

var signatureForm = (function () {
    var pki = null;
    var formElements = {};

    // -------------------------------------------------------------------------------------------------
    // Function called once the page is loaded
    // -------------------------------------------------------------------------------------------------
    var init = function (fe) {
        formElements = fe;

        // Wire-up of button clicks
        formElements.signButton.click(sign);
        formElements.refreshButton.click(refresh);

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
    var refresh = function () {
        loadCertificates();
    };

    // -------------------------------------------------------------------------------------------------
    // Function that loads the certificates, either on startup or when the user
    // clicks the "Refresh" button. At this point, the UI is already blocked.
    // -------------------------------------------------------------------------------------------------
    var loadCertificates = function () {
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
        }).success(function () {});
    };

    // -------------------------------------------------------------------------------------------------
    // Function called when the user clicks the "Sign" button
    // -------------------------------------------------------------------------------------------------
    var sign = function () {
        // Get the thumbprint of the selected certificate
        var selectedCertThumbprint = formElements.certificateSelect.val();
        var toSignHashField = $("#toSignHashField").val();

        if (lockScreen !== undefined) {
            lockScreen();
        }

        pki.readCertificate(selectedCertThumbprint).success(function (certificate) {
            $(".certificateField").val(certificate);
            pki.signHash({
                thumbprint: selectedCertThumbprint,
                hash: toSignHashField,
                digestAlgorithm: 'SHA-1'
            }).success(function (signature) {
                $(".signatureField").val(signature);
                rc();
            });
        });
    };

    // -------------------------------------------------------------------------------------------------
    // Function called if an error occurs on the Web PKI component
    // -------------------------------------------------------------------------------------------------
    var onWebPkiError = function (message, error, origin) {
        // Log the error to the browser console (for debugging purposes)
        if (console) {
            console.log('An error has occurred on the signature browser component: ' + message, error);
        }
        // Show the message to the user. You might want to substitute the alert below with a more user-friendly UI
        // component to show the error.
        alert(message);
        unlockScreen();
    };

    return {
        init: init
    };

})();
