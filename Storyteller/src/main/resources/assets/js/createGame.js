$(function () {
    $('#createGame').on('click', function () {
        $.ajax({
            type: "GET",
            url: "./api/game/createGame",
            data: {
                gameName: $("#gameName").val(),
                gamePassword: $("#passwordInput").val(),
                literatureMode: $("#literatureMode").is(":checked")
            },
            success: function(data) {
                window.location.href = "gameLobby.html?" + data.playerId.string;
            }
        });
    });
});
