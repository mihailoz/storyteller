$(function () {
    var s = "";

    $("#startGame").click(function () {

        $(".imgs").addClass('scale');
        $("#startGame").fadeOut(2200);
        $("#rules").fadeOut(2200);
        $("#createGame").fadeOut(2200);
        $("#listGames").fadeOut(2200);
        setTimeout(function () {
            window.location.href = "inGame.html"
        }, 4000);

    });

    $("#createGame").on("click",function () {
      $('#kreiraj').modal("show");
    });

    $("#listGames").click(function () {
        $('#nadji').modal("show");
    });

});
