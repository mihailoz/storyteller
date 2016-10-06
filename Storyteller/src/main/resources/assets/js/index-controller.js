$(function () {
    var s = "";

    $("#startGame").click(function () {

        $(".imgs").addClass('scale');
        $("#startGame").fadeOut(2200);
        $("#rules").fadeOut(2200);
        $("#createGame").fadeOut(2200);
        $("#listGames").fadeOut(2200);
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

    $("#openCreateGame").on("click",function () {
      $('#kreiraj').modal("show");
    });

    $("#openListGames").click(function () {
        $('#nadji').modal("show");
    });

});
