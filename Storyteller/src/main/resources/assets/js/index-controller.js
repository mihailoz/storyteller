$(function () {
    var s = "";

    $("#startGame").click(function () {

        $(".imgs").addClass('scale');
        $("#startGame").fadeOut(2200);
        $("#rules").fadeOut(2200);
        $("#openCreateGame").fadeOut(2200);
        $("#openListGames").fadeOut(2200);
        setTimeout(function () {
            $.ajax({
                url: "./api/play",
                dataType: "text",
                success: function(data) {
                    window.location.href = "inGame.html?" + data;
                }
            });

        }, 4000);

    });
    
    var echoWebsocket = new WebSocket("ws://" + window.location.host + "/websocket/play");
    
    echoWebsocket.onopen = function (event) {
        var data = {
            code: 'start-game',
            playerId: 'some playerId',
            data: {
                'message': 'some message',
                'message2': 'some other message'
            }
        };
        echoWebsocket.send(JSON.stringify(data));
    };

    $("#openCreateGame").on("click",function () {
      $('#kreiraj').modal("show");
    });

    $("#openListGames").click(function () {
        $('#nadji').modal("show");
    });
$("#english").click(function(){
  location.href = "index.html";
});
$("#german").click(function(){
  location.href = "indexgerman.html";
});
$("#russian").click(function(){
  location.href = "indexrussian.html";
});
});
