const themeConfig = new ThemeConfig();
themeConfig.initTheme();

$(function(){
    $("#nav-placeholder").load("nav.html", function(){
        $(".custom-control-input")
            .prop("checked", themeConfig.getTheme() == "dark")
            .on("change", function(){
                if($(this).prop('checked')){
                    themeConfig.setTheme("dark");
                }
                else {
                    themeConfig.setTheme("light");
                }
                updateToTheme();
            });
        updateToTheme();

        // Set login-button content
        if($("#loginInfo").length > 0){
            $("#navLogin").html( $("#loginInfo").html() );
        }
    });
});

function updateToTheme(){
    if(themeConfig.getTheme() == "dark"){
        $("table").addClass("table-dark");
    }
    else {
        $("table").removeClass("table-dark");
    }
}

function validateTable(tableid){
    $("#"+tableid+" tbody tr").each(function(key, val){
        validateTableRow(tableid, "row"+key);
    })
}

function validateTableRow(tableid, tablerow){
    if( $("#"+tableid+" ."+tablerow+" input:invalid").length > 0){
        $("#"+tableid+" ."+tablerow).css("background-color", "#ee00003d");
    }
    else {
        $("#"+tableid+" ."+tablerow).css("background-color", "");
    }
}