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
