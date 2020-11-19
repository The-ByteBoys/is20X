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

function deleteRow(that){
    if(that.parent().children().length <= 1){
        return;
    }

    let skipConfirm = true;
    that.find("input").each(function (){
        if($(this).val() !== ""){
            skipConfirm = false;
        }
    });
    if(skipConfirm || confirm("Are you sure you want to delete this row?")){
        that.remove();
    }
}

function addNewRow(that){
    // let iterator = Number( that.attr("class").replace("row", "") )+1;
    that.clone()
        .find("input").each(function() {
            $(this).val('');
        })
        // .attr("class", "row"+iterator)
        .end().insertAfter(that);
}

function addTable(title, type){
    let newHtml;

    newHtml = "<h3>Ny tabell: "+(type==='senior'?"Senior": type==='jA'?"Junior A": type==="jB"?"Junior B": type==='jC'?"Junior C":type)+"</h3>";

    newHtml += "<div id='tableNew'>\n";
    newHtml += "  <form method=\"post\" id=\"tableFormNew\" action=\"postExcel\" target=\"_blank\">\n";
    newHtml += "    <div class=\"\">\n";
    newHtml += "      <table class=\"table table-striped table-bordered"+(themeConfig && themeConfig.getTheme() === "dark"?" table-dark":"")+"\">\n";
    newHtml += "        <thead>\n";
    newHtml += "          <tr><th></th>";
    $.each(getKeySet(type), function (key, value){
        newHtml += "            <th>"+beautifyTitle(value)+"</th>\n";
    });
    newHtml += "          </tr>";
    newHtml += "        </thead>\n";
    newHtml += "        <tbody>\n";
    newHtml += "          <tr class=\"row1\">";
    newHtml += "<td><img src='img/search.svg' style='cursor: pointer; height: 21px;' alt='se' class='searchBtn svgIcon' /></td> ";
    $.each(getKeySet(type), function (key, value){
        switch (value) {
            case "fname":
                newHtml += "<td><input type=\"text\" name=\"fname\" value=\"\" class=\"form-control  longInput\"></td>";
                break;
            case "lname":
                newHtml += "<td><input type=\"text\" name=\"lname\" value=\"\" class=\"form-control  longInput\" required></td>";
                break;
            case "birth":
                newHtml += "<td><input type=\"date\" name=\"birth\" value=\"\" class=\"form-control \""+(type!=='senior'?" required":"")+"></td>";
                break;
            case "clubs":
                newHtml += "<td><input type=\"text\" name=\"clubs\" value=\"\" class=\"form-control\"></td>";
                break;
            case "5000Tid":
            case "3000Tid":
            case "2000Tid":
                newHtml += "<td><input type=\"text\" name=\""+value+"\" value=\"\" class=\"form-control\" pattern=\"[0-9]+:[0-9]{1,2}(\\.[0-9]*)?\"></td>";
                break;
            default:
                newHtml += "<td><input type=\"text\" name=\""+value+"\" value=\"\" class=\"form-control\" pattern=\"[0-9-]+(\\.[0-9]*)?\"></td>";
        }
    });
    newHtml += "\n          <td><img src='img/add.svg' onClick='addNewRow($(this).parent().parent());' style='cursor: pointer; height: 21px;' alt='+' class='svgIcon' /><img src='img/remove.svg' onClick='deleteRow($(this).parent().parent());' style='cursor: pointer; height: 21px;' alt='-' class='svgIcon' /></td>\n          </tr>\n";
    newHtml += "        </tbody>\n";
    newHtml += "      </table>\n";

    newHtml += "      <div class='form-row container-sm' style='margin: auto; width: 40vw;'>\n";
    newHtml += "        <select class='col sexPicker form-control' name=\"sex\" style='width: initial; display: initial; min-width: 120px;'> <option value='-'>Velg kjønn</option> <option value='M'>Menn</option> <option value='F'>Kvinner</option> <option value='O'>Andre</option> </select>\n";
    newHtml += "        <input type='date' class='col form-control' style='min-width: 120px;' />\n";
    newHtml += "        <input type='submit' value='Submit all' class='col form-control' style='width: initial; display: initial; min-width: 120px;' >\n";
    newHtml += "      </div>\n";
    newHtml += "    </div>\n";
    newHtml += "  </form>\n";
    newHtml += "</div>";

    $("#tableHolder").append(newHtml);
}

function getKeySet(type){
    let newArray = ["fname", "lname", "birth", "clubs"];
    switch (type){
        case "senior":
            newArray.push("5000Watt");
            newArray.push("5000Tid");
            newArray.push("2000Watt");
            newArray.push("2000Tid");
            newArray.push("60Watt");
            newArray.push("liggroProsent");
            newArray.push("liggroKg");
            newArray.push("knebProsent");
            newArray.push("knebKg");
            newArray.push("antBev");
            break;
        case "jA":
            newArray.push("5000Watt");
            newArray.push("5000Tid");
            newArray.push("2000Watt");
            newArray.push("2000Tid");
            newArray.push("60Watt");
            newArray.push("liggroProsent");
            newArray.push("liggroKg");
            newArray.push("cmSargeant");
            newArray.push("antBev");
            break;
        case "jB":
            // newArray.push("3000Total");
            newArray.push("3000Tid");
            newArray.push("2000Watt");
            newArray.push("2000Tid");
            newArray.push("60Watt");
            newArray.push("kroppshev");
            newArray.push("cmSargeant");
            newArray.push("antBev");
            break;
        case "jC":
            newArray.push("3000Tid");
            newArray.push("60Watt");
            newArray.push("kroppshev");
            newArray.push("antBev");
            break;
    }

    return newArray;
}

function beautifyTitle(input){
    switch (input) {
        case "fname":
            return "Fornavn";
        case "lname":
            return "Etternavn";
        case "birth":
            return "Fødselsår";
        case "clubs":
            return "Klubber";
        case "5000Watt":
            return "5000 Watt";
        case "5000Tid":
            return "5000 Tid";
        case "3000Total":
            return "3000 Total";
        case "3000Tid":
            return "3000 Tid*";
        case "2000Watt":
            return "2000 Watt";
        case "2000Tid":
            return "2000 Tid";
        case "60Watt":
            return "60 Watt";
        case "liggroProsent":
            return "Liggende rotak %";
        case "liggroKg":
            return "Liggende rotak KG";
        case "kroppshev":
            return "Kroppshevinger";
        case "knebProsent":
            return "Knebøy %";
        case "knebKg":
            return "Knebøy KG";
        case "cmSargeant":
            return "Sargeant CM";
        case "antBev":
            return "Antall Bevegelser";
        default:
            return input;
    }
}

$(document).ready(function(){
    $("#addNewTable").on('submit', function(e){
        e.preventDefault();
        addTable("New table", $("#addNewTable select").val());
        attachSearchListeners();
    })

    attachSearchListeners();
});

function getSearchResultHtml(left, top, rowId){
    let html = "<div id='searchResultTable' style='display: block;position: absolute;top: "+top+"px; left: "+left+"px; border-radius: 5px;border: 1px solid;'>" + //background: "+$("body").css('background-color')+"
        "<table id='"+rowId+"' class='table table-striped table-bordered table-light table-hover' style='margin-bottom: 0;'><thead><tr><th>Fornavn</th><th>Etternavn</th><th style='padding-right: 20px;'>Fødselsdato</th></tr></thead><tbody></tbody></table>" +
        "<button type='button' class='close' aria-label='Close' style='position: absolute; top: 0px; right: 5px;' onclick='$(this).parent().remove();'><span aria-hidden='true'>×</span></button>" +
        "</div>";

    return html;
}

function attachSearchListeners(){
    $(".searchBtn").unbind().on('click', function(e){
        buildSearchBlock($(this).parent().parent());
    });
}

let timeOut = null;
let currentRow = null;

function buildSearchBlock(parentRow){
    let firstNameInput = parentRow.find("input[name=fname]");
    let lastNameInput = parentRow.find("input[name=lname]");

    currentRow = parentRow;

    $(firstNameInput).on('keyup', function(){
        updateSearch(firstNameInput.val()+" "+lastNameInput.val());
    });
    $(lastNameInput).on('keyup', function(){
        updateSearch(firstNameInput.val()+" "+lastNameInput.val());
    });

    let pos = parentRow.offset();
    let posX = pos.left;
    let posY = pos.top+parentRow.height();

    $("#searchResultTable").remove();
    $(getSearchResultHtml(posX, posY, parentRow.attr("id"))).appendTo("body");
    updateToTheme();
    updateSearch(firstNameInput.val()+" "+lastNameInput.val());
}

function updateSearch(search){
    let searchResult = $("#searchResultTable").find("tbody");
    searchResult.empty();

    if(search.length <= 3){
        searchResult.append("<tr><td colspan='3'>Start å skrive et navn for å søke</td></tr>");
    }
    else {
        searchResult.append("<tr><td colspan='3'>Loading...</td></tr>");
        if(timeOut != null){ clearTimeout(timeOut); }
        timeOut = setTimeout(function() {
            searchResult.empty();
            $.post("api/utover/", {search: search}, function (data) {
                    if (typeof data["data"] === "undefined" || data["data"].length === 0) {
                        searchResult.append("<tr><td colspan='3'>Ingen resultater</td></tr>");
                    } else {
                        $.each(data["data"][0], function (key, val) {
                            searchResult.append("<tr onclick='selectRow(this);'><td class='fname'>" + val.fname + "</td><td class='lname'>" + val.lname + "</td><td class='birth'>" + val.birth + "</td></tr>");
                        });
                    }
                },"json");
        }, 800);
    }
}

function selectRow(that){
    currentRow.find("input[name=fname]").val($(that).find("td.fname").html());
    currentRow.find("input[name=lname]").val($(that).find("td.lname").html());
    currentRow.find("input[name=birth]").val($(that).find("td.birth").html());

    $("#searchResultTable").remove();
    validateTable(currentRow.parent().parent().parent().parent().attr("id"));
}