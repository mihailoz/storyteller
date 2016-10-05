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

                    var $tablerow = '<tr class="gameRow"><td>' + g.gameName + '</td><td>' + g.playerNumber + '</td><td>' + g.passwordProtected + '</td><td><button class="joinGameButton" id="' + gameData.gameId + '">Join</button></td></tr>';
                    $('#gameTable tr:last').after($tablerow);
                }
                
                $(".joinGameButton").on('click', function () {
                   // TODO 
                });
            }
        });

    };

    $("#searchInput").on("change", function () {
        var searchTerm = $("#seachInput").val();

        searchGames(searchTerm);
    });
});