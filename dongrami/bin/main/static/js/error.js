            window.onload = function() {
                var errorCode = getErrorCodeFromUrl();
                setErrorMessage(errorCode);
            };

            function getErrorCodeFromUrl() {
                var path = window.location.pathname;
                var parts = path.split('/');
                var lastSegment = parts[parts.length - 1];
                if (!isNaN(parseInt(lastSegment))) {
                    return parseInt(lastSegment);
                }
                return null;
            }

            function setErrorMessage(errorCode) {
                var errorMessage = document.getElementById('msg');
                if (errorMessage && errorCode !== null) {
                    errorMessage.textContent = errorCode + ' Error 발생';
                }
            }