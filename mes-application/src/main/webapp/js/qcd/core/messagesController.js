var QCD = QCD || {};

var pnotify_stack = {"dir1": "up", "dir2": "left", "firstpos1": 15, "firstpos2": 30};

QCD.MessagesController = function() {

	this.clearMessager = function() {
		$.pnotify_remove_all()
	}
	
	this.addMessage = function(type, message) { // type = [info|error|success]
		message = QCD.MessagesController.split(message, type);
		
		$.pnotify({
			pnotify_title: message[0],
			pnotify_text: message[1],
			pnotify_stack: pnotify_stack,
			pnotify_history: false,
			pnotify_width: "300px",
			pnotify_type: type,
			pnotify_addclass: type == 'success' ? 'ui-state-success' : '',
			pnotify_notice_icon: type == 'success' ? 'ui-icon ui-icon-success' : 'ui-icon ui-icon-notify',
			pnotify_error_icon: 'ui-icon ui-icon-error',
			pnotify_opacity: .9,
			pnotify_delay: 4000,
			pnotify_hide: true // type == 'error' ? false : true
		});
		
	}

}

QCD.MessagesController.split = function(message, type) {
	contents = message.split('\n')
	
	if(contents.length > 1) {
		title = contents[0];
		text = contents.splice(1).join("\n")
	} else {
		title = type == 'error' ? 'Wystąpił błąd' : (type == 'notice' ? 'Ważna informacja' : 'Operacja zakończona pomyślnie'); // TODO masz i18n
		text = contents[0];
	}

	return [ title, text ];
}