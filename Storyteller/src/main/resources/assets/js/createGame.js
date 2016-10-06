$(function () {
    $('#createGame').on('click', function () {
        $.ajax({
            type: "GET",
            url: "./api/game/createGame",
            data: {
                gameName: $("#gameName").val()
            },
            success: function(data) {
                window.location.href = "gameLobby.html?" + data.playerId.string;
            }
        });
    });
});
