function loadUserData() {
   PreferencesInterface.getUserData(function(userJson) {
        var user = JSON.parse(userJson);
        document.getElementById('name_field').value = user.name;
        document.getElementById('surname_field').value = user.surname;
        document.getElementById('age_field').value = user.age;
        document.getElementById('height_field').value = user.height;
        document.getElementById('married_checkbox').checked = user.married;
    });
}

function saveUserData() {
    var user = getUserData();
    PreferencesInterface.saveUserData(user);
    AndroidInterface.showToastMessage("Saved");
}

function getUserData() {
    var nameField = document.getElementById('name_field');
    var surnameField = document.getElementById('surname_field');
    var ageField = document.getElementById('age_field');
    var heightField = document.getElementById('height_field');
    var marriedField = document.getElementById('married_checkbox');

    var user = {
        name: nameField.value,
        surname: surnameField.value,
        age: ageField.value != "" ? ageField.value : 0,
        height: heightField.value != "" ? heightField.value : 0,
        married: marriedField.checked

    };

    return JSON.stringify(user);
}

loadUserData();