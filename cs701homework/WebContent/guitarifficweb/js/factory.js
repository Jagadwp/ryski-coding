/*
 * author: Ryszard Kilarski 
 * email: emrys@bu.edu 
 * BU ID: U81-39-8560
 * 
 * This is the factory object to create complex DOM elements.
 */
factory = {

	/**
	 * Create a lyric text row item.
	 */
	createTextRow : function(text) {
		if (text == null) {
			text = '';
		}
		var newRow = $('<tr/>').html($('<td/>').html(factory.createTextElement(text)));
		return newRow;
	},

	createTextElement : function(text) {
		var newElement = $('<input/>').attr({
			type : 'text',
			placeholder : 'lyrics',
			value : text
		}).addClass('songtext').on('keyup', handlers.textKeyHandler).hover(
				handlers.hoverStartLyrics, handlers.hoverEndLyrics);
		return newElement;
	},
	/**
	 * Create a button to launch the 'new song' functionality.
	 */
	createNewSongItem : function() {
		var newItem = $('<div/>').addClass('loaditem').attr('id', 'newsong').html('New Song').on(
				'click', handlers.newSongHandler);
		return newItem;
	},

	/**
	 * Create a button to launch the 'load song' functionality.
	 */
	createSongItem : function(song) {
		var newItem = $('<div/>').addClass('loaditem').append(
				$('<div/>').addClass('songname').html(song.songName)).append(
				$('<div/>').addClass('artistname').html('by<br>' + song.artistName)).on('click',
				function() {
					return handlers.loadSongHandler(song);
				});
		return newItem;
	},

	/**
	 * Create a button to launch the 'delete chord database' functionality.
	 */
	createResetDatabaseItem : function() {
		var newItem = $('<div/>').attr({
			'class' : 'loaditem',
			'id' : 'resetdatabase'
		}).html('Delete Chord Database');
		$(newItem).on('click', handlers.resetDatabaseHandler);
		return newItem;
	},

	/**
	 * Create a button to launch the 'recreate chord database' functionality.
	 */
	recreateChordDatabaseItem : function() {
		var newItem = $('<div/>').attr({
			'class' : 'loaditem',
			'id' : 'recreate'
		}).html('Recreate Chord Database');
		$(newItem).on('click', handlers.recreateDatabaseHandler);
		return newItem;
	},

	/**
	 * Create a button to launch the 'reset songs' functionality.
	 */
	createResetSongsItem : function() {
		var newItem = $('<div/>').attr({
			'class' : 'loaditem',
			'id' : 'resetsongs'
		}).html('Delete All Songs');
		$(newItem).on('click', handlers.deleteAllSongs);
		return newItem;
	},

	/**
	 * Create a list to contain chords.
	 */
	createDiagramList : function(newId) {
		var newItem = $('<ol/>').addClass('chordlist').attr('id', newId);
		$(newItem).sortable({
			connectWith : '.chordlist',
			placeholder : 'guitarcharthighlight',
			forcePlaceholderSize : true
		});
		return newItem;
	},

	/**
	 * Create new ordered list in DOM.
	 * 
	 * @returns the id of the new list.
	 */
	createNewList : function() {
		// Get the previous list to insert after.
		var newid;
		// If we don't have a previous list, need to create a new one.
		if (prev == undefined) {
			newid = 'chordlist1';
		} else {
			// Parse the number out of the previous list's id.
			newid = 'chordlist' + (parseInt(prev.match(/\d+$/), 10) + 1);
		}

		// Create new list with new id and insert into DOM.
		var newList = factory.createDiagramList(newid);
		$('#chordarea').append(newList);
		return newid;
	},

	/**
	 * Create a new id for the chord list.
	 */
	createNewChordListId : function() {
		var prev = $('#chordarea').children().last().attr('id');

		var newid;
		// If we don't have a previous list, need to create a new one.
		if (prev == undefined) {
			newid = 'chordlist1';
		} else {
			// Parse the number out of the previous list's id.
			newid = 'chordlist' + (parseInt(prev.match(/\d+$/), 10) + 1);
		}
		return newid;
	},

	/**
	 * Create the search by song or artist field.
	 */
	createSearchBy : function() {
		var div = $('<div/>').attr('id', 'searchby').append($('<input/>').attr({
			'id' : 'searchbysongorartist',
			'placeholder' : 'search by song or artist',
			'type' : 'text'
		}).on('keyup', function() {
			$('#loadarea .loaditem').remove();
			dao.fetchSongs($(this).val(), dom.loadSongIntoTray);
		}));
		return div;
	},
	createFingeringSelect : function(finger) {
		var item = $('select').attr('id', "fingering" + finger);
		for ( var i = 0; i < 6; i++) {
			$(item).append(createFingeringOption(i));
		}
		return item;

		/**
		 * Inner function to create each option.
		 */
		function createFingeringOption(option) {
			if (option == 0) {
				option = ' ';
			}
			var item = $('option').attr('value', option).val(option);
			return item;
		}
	},
	createFretRadio : function(string, fret) {
		var item = $('input').attr({
			'type' : 'radio',
			'name' : 'string' + string,
			'value' : fret
		});
		return item;
	}
};