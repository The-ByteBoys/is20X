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