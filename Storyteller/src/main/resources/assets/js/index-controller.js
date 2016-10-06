$(function () {
    var s = "";

    $("#startGame").click(function () {

        $(".imgs").addClass('scale');
        $("#startGame").fadeOut(2200);
        $("#rules").fadeOut(2200);
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

    $("#createGame").click(function () {
        $(".imgs").addClass('scale');
        $("#startGame").fadeOut(2200);
        $("#rules").fadeOut(2200);
        setTimeout(function () {
            window.location.href = "createGame.html"
        }, 4000);
    });

    $("#listGames").click(function () {
        $(".imgs").addClass('scale');
        $("#startGame").fadeOut(2200);
        $("#rules").fadeOut(2200);
        setTimeout(function () {
            window.location.href = "listGames.html"
        }, 4000);
    });
});
