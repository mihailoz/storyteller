$(function () {
    var s = "";

    $("#btn1").click(function () {

        $(".imgs").addClass('scale');
        interv = setInterval(function () {
            window.location.href = "inGame.html"
        }, 4000);

        
    });
});