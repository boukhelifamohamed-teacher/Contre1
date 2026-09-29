var exec = require('cordova/exec');

module.exports = {
  print: function (jobName, orientation, success, error) {
    exec(success, error, 'NativePrint', 'print', [
      jobName || 'تسيير الاختبارات الفصلية',
      orientation || 'portrait'
    ]);
  }
};
