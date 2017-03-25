$(document).ready(function()
{
	$('#templateMovieCardContainer').remove();
	
	var templateMovieCard = $(
			'<div id="templateMovieCardContainer">' +
				'<div id="templateMovieCardContent"></div>' +
			'</div>');
	
	$('body').append(templateMovieCard);
	
	// Hide card by default
	$('#templateMovieCardContainer').hide();
	
	var movieid;
	
	$('.movieCardAnchor').mouseenter(function() {
		

		$('#templateMovieCardContainer').hide();
		
		movieid = $(this).attr('id');
		
		$.get('moviecard', {movie_id: movieid}, function(responseText) {
			
            $('#templateMovieCardContent').html(responseText);
            $('#templateMovieCardContainer').fadeIn();
        });
		 
		var pos = $(this).offset();
	    var height = $(this).height();
	    
	    $('#templateMovieCardContainer').css({
	        top: pos.top + height + 10 + 'px',
	        left: pos.left + 'px'
	    });
		
	});

	$(document).click(function() {
	    $('#templateMovieCardContainer').hide();
		$('#templateMovieCardContent').html("");
	});
	
	// $('.movieCardAnchor').mouseleave(function() {
		
	// 	if ($(this).attr('id') == movieid)
	// 	{
	// 		$('#templateMovieCardContainer').hide();
	// 		$('#templateMovieCardContent').html("");
	// 	}
	// });
	
});