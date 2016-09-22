$(function () {
    var s = "";

    $("#startGame").click(function () {

        $(".imgs").addClass('scale');
        $("#startGame").fadeOut(2000);
        interv = setInterval(function () {
            window.location.href = "inGame.html"
        }, 4000);

        
    });
});