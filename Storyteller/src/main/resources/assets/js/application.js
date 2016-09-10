$(function() {
    var playerId;

    var startGame = function() {
        var socket = atmosphere;

        console.log(socket);
        var request = {
            url: document.location.toString() + 'play/' + playerId,
            contentType: "application/json",
            logLevel: 'debug',
            transport: 'websocket',
            fallbackTransport: 'long-polling'
        };

        request.onOpen = function(response) {
            console.log(response);
        };

        request.onMessage = function(response) {
            console.log(response);
        };

        var subSocket = socket.subscribe(request);

        $('#nekiInput').keydown(function(e) {
            if (e.keyCode === 13) {
                var msg = $(this).val();

                subSocket.push(msg);
                $(this).val('');
            }
        });
    };

    $("#findGame").click(function() {
        $.ajax({
            url: "./api",
            dataType: "text",
            success: function(data) {
                playerId = data;
                startGame();
            }
        });
    });
});
