DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_movie`(
	IN `_title` varchar(100),
	in `_year` int(11),
	in `_director` varchar(100),
	in `_banner_url` varchar(200),
	in `_trailer_url` varchar(200),
    in `_genre` varchar(50),
    in `_first_name` varchar(25),
    in `_last_name` varchar(25)
)
add_movie:BEGIN
    if _title in (select title from movies) then 
		select -1;
        leave add_movie;
    end if;
	
    if _title = '' then 
		select -2;
        leave add_movie;
    end if;
    
    if _year = '' then 
		select -2;
        leave add_movie;
    end if;
    
    if _director = '' then 
		select -2;
        leave add_movie;
    end if;
    
	insert into movies
	(
        title,
        year,
        director,
        banner_url,
        trailer_url
	)
    values
    (
		_title,
        _year,
        _director,
        _banner_url,
        _trailer_url
    );
    
    # _genre is not and empty string
    if  _genre != '' then
		# if _genre does not already exist then add it to genres
		if _genre in (select name from genres) then
			insert into genres (name) values (_genre);
        end if;
        
        # create link between movie and _genre
		insert into genres_in_movies
        (
			genre_id,
            movie_id
        )
		select 
			(select id from genres where name = _genre limit 1) as genre_id,
			(select id from movies where title = _title limit 1) as movie_id;
	end if;
    
    # if _first_name and _last_name are not empty strings
    if  _first_name != '' and _last_name != '' then
    
		# if star is not in stars then create a new star and insert into stars
        if (_first_name, _last_name) not in (select first_name, last_name from stars) then
			insert into stars (first_name, last_name) values (_first_name, _last_name);
        end if;
        
        # create link between movie and star
		insert into stars_in_movies
        (
			star_id,
            movie_id
        )
		select 
			(select id from stars where first_name = _first_name and last_name = _last_name limit 1) as star_id,
			(select id from movies where title = _title limit 1) as movie_id;
	end if;
    select 1;
END$$
DELIMITER ;
