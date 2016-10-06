$(function () {
    var searchGames = function (searchTerm) {

        $.ajax({
            url: "api/game/listGames",
            data: {
                q: searchTerm
            },
            success: function (data) {
                var gameData = [];

                for (var i = 0; i < data.length; i++) {
                    gameData.push(JSON.parse(data[i]));
                }

                $("#gameTable tr.gameRow").remove();

                for (var i = 0; i < gameData.length; i++) {
                    var g = gameData[i];
                    var $tablerow = '<tr class="gameRow" id="' + g.gameId + '"><td>' + g.gameName + '</td><td>' + g.playerNumber + '</td><td>' + g.passwordProtected + '</td><td><button class="joinGameButton" id="' + g.gameId + '">Join</button></td></tr>';
                    $('#gameTable tr:last').after($tablerow);
                }

                $(".joinGameButton").on('click', function (e) {
                   $.ajax({
                       type: "GET",
                       url: "./api/game/joinGame",
                       data: {
                           gameId: $(e.target).attr('id')
                       },
                       complete: function (data) {
                           if(data.responseText !== "Password incorrect") {
                               window.location.href = "gameLobby.html?" + data.responseText;
                           }
                       }
                   });
                });
            }
        });

    };

    $("#searchInput").on("change", function () {
        var searchTerm = $("#searchInput").val();

        searchGames(searchTerm);
    });

    $('#nadji').on('shown.bs.modal', function (e) {
        searchGames("");
    });
});
