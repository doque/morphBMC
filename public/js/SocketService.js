app.factory('SocketService', ['$q', '$rootScope', '$location', function($q, $rootScope, $location) {

    // Save established socket (connected, ready to send)
    var establishedSocket;

    // SocketStatus
    var status = 'disconnected';

    // Only use the promise: will send as soon as it is available
    // Reason: when trying to send messages without established connection exception is thrown
    var socketPromise = function() {
        var deferred = $q.defer();
        if(establishedSocket === undefined) {
            status = 'connecting';
            establishedSocket = new WebSocket('ws://' + $location.host() + ":" + $location.port() + '/ws/open/'+window.PROBLEM_ID +'/'+window.USER_ID);
            // Attach listener instantly
            establishedSocket.onmessage = function(message) {
                $rootScope.$apply(function() {
                    notifyAll(message);
                });
            };
            establishedSocket.onopen = function() {  
                console.log("Socket connection established");
                $rootScope.$apply(function() {
                    status = 'connected';
                });
            };
            establishedSocket.onerror = function() {
                console.log("Socket connection closed due to error");
                $rootScope.$apply(function() {
                    status = 'disconnected (error)';
                    establishedSocket = undefined;
                });
            }
            establishedSocket.onclose = function() {
                console.log("Socket connection closed by server");
                $rootScope.$apply(function() {
                    status = 'disconnected (closed by server)';
                    establishedSocket = undefined;
                });
            }  
        } else if(establishedSocket.readyState === 1) {
            deferred.resolve(establishedSocket);
        }
        return deferred.promise;
    }
    // Establish connection when building the service
    socketPromise();
    
    function notifyAll(incoming) {
        // If data is not parsable (i.e. empty response to getLastMessage) don't do anything
       /* try {
            var data = JSON.parse(incoming.data);    
        } catch (TypeError) {
            console.log("malformed json")
        }
        var type = data.type;
        var message = data.message;
        var args = data;*/
        console.log("socket.in", incoming);
        //$rootScope.$broadcast(type, args);
    };

    var send = function(message) {
        socketPromise().then(function(socket) {
            console.log("socket.out", message);
            socket.send(message);
        });
    }

    return {
        send: function(message) {
            send(message);
        },
        status: function() {
            return status;
        },
        reconnect: function() {
            if(status !== 'connected' && status !== 'connecting') {
                console.log('Trying to reestablish socket connection...');
                socketPromise();
            }
        }

    };
}])
