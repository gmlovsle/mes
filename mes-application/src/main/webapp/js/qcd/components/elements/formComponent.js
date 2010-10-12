var QCD = QCD || {};
QCD.components = QCD.components || {};
QCD.components.elements = QCD.components.elements || {};

QCD.components.elements.FormComponent = function(_element, _mainController) {
	$.extend(this, new QCD.components.Component(_element, _mainController));

	var mainController = _mainController;

	var element = _element;

	var component = $("#" + element.attr('id'));

	var errorIcon = $("#" + element.attr('id') + "_error_icon");

	var errorMessages = $("#" + element.attr('id') + "_error_messages");

	errorIcon.hover(function() {
		errorMessages.show();
	}, function() {
		errorMessages.hide();
	});
	
	this.input = $("#" + element.attr('id') + "_input");

	this.getComponentData = function() {
		return {
			value : this.input.val()
		}
	}

	this.setComponentData = function(data) {
		this.input.val(data.value);
	}

	this.getComponentValue = function() {
		return this.getComponentData()
	}

	this.setComponentValue = function(value) {
		this.setComponentData(value);
		setComponentRequired(value.required);
	}

	this.setComponentState = function(state) {
		this.setComponentData(state);
		setComponentRequired(state.required);
	}

	this.setComponentEnabled = function(isEnabled) {
		if (isEnabled) {
			this.input.removeAttr('disabled');
		} else {
			this.input.attr('disabled', 'true');
		}
	}

	function setComponentRequired(isRequired) {
		if (isRequired) {
			component.addClass("required");
		} else {
			component.removeClass("required");
		}
	}

	function setComponentError(isError) {
		if (isError) {
			component.addClass("error");
		} else {
			component.removeClass("error");
		}
	}

	this.setMessages = function(messages) {
		errorMessages.html("");

		for ( var i in messages.error) {
			messageDiv = $('<div>')

			message = QCD.MessagesController.split(messages.error[i], 'error');

			messageDiv.append('<span>' + message[0] + '</span>');
			messageDiv.append('<p>' + message[1] + '</p>');

			errorMessages.append(messageDiv);
		}

		setComponentError(messages.error.length != 0);
	}

	this.setComponentLoading = function(isLoadingVisible) {
	}

}