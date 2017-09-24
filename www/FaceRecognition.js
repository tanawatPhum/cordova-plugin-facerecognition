// var exec = require('cordova/exec');

// exports.process = function(processId, params, title,success, error) {
//     console.log("Hello World");
//     exec(success, error, "OpenCVActivity", "process", [processId, params, title]);
// };

var FaceRecognition = function() {
	var startRecognizer = function(url,success, error) {
		console.log("facerecognition javascipt side");
		if (cordova.exec)
			cordova.exec(success, error, 'OpenCVActivityPlugin', 
			   'process',[url]);
		else
			error("Cordova not present");
	};
	return {
		startRecognizer: startRecognizer
	};
}();

module.exports = FaceRecognition;