$(function () {
    var s = "";

    $("#btn1").click(function () {

        $(".imgs").addClass('scale');
        setTimeout(function () {
            window.location.href = "index2.html"
        }, 4000);

    });

    $("#unesi").click(function () {
        s += $("#inp").val() + " ";
        $("#txt").text(s);

        $("#inp").val("");
        $("#inp").focus();

    });

});