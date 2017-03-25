// $(document).ready(function() {
//         $(function() {
//                 $("#searchBar").autocomplete({     
//                 source : function(request, response) {
//                 $.ajax({
//                         url : "searchbar",
//                         type : "GET",
//                         data : {
//                                 term : request.term
//                         },
//                         dataType : "json",
//                         success : function(data) {
//                                 response(data);
//                         }
//                 });
//         }
// });
// });
// });


$(document).ready(function() {
        
    var dropdown = $('<div id="autocompleteDropdown"></div>');
        
    $('body').append(dropdown);
        
    $('#autocompleteDropdown').hide();
        
    var pos = $('#searchBar').offset();
    var height = $('#searchBar').height();
    var width = $('#searchBar').width();
    
    $('#autocompleteDropdown').css({
        width: width + 5 + 'px',
        top: pos.top + height + 10 - 2 + 'px',
        left: pos.left - 4 + 'px',
    });
        
    $('#searchBar').focus(function() {
        $('#autocompleteDropdown').fadeIn();
    });
        
        $('#searchBar').blur(function() {
        $('#autocompleteDropdown').fadeOut();
    });
        
});

function lookup(text) 
{
    if (text.length == 0) 
    {
        $('#autocompleteDropdown').fadeOut();
    } 
    else 
    {
        $.get("searchbar", {term: text}, function(list) {
                        
        $('#autocompleteDropdown').html(list);
        $('#autocompleteDropdown').fadeIn();
        });
    }
};