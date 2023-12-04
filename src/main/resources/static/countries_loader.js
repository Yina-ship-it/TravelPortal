let countriesTableVisible = false;

function toggleCountriesTable() {
    if (!countriesTableVisible) {
        loadCountries();
        document.getElementById('countriesTable').style.display = 'table';
        document.getElementById('countriesLoaderButton').textContent = 'Скрыть страны';
    } else {
        document.getElementById('countriesTable').style.display = 'none';
        document.getElementById('countriesLoaderButton').textContent = 'Загрузить страны';
    }

    countriesTableVisible = !countriesTableVisible;
}

function loadCountries() {
    fetch('/api/countries/')
        .then(response => response.json())
        .then(data => {
            displayCountries(data);
        })
        .catch(error => console.error('Ошибка при загрузке стран:', error));
}

function displayCountries(countries) {
    const tableBody = document.querySelector('#countriesTable tbody');
    tableBody.innerHTML = '';

    countries.forEach(country => {
        const row = document.createElement('tr');

        const nameCell = document.createElement('td');
        nameCell.textContent = country.name;
        row.appendChild(nameCell);

        const capitalCell = document.createElement('td');
        capitalCell.textContent = country.capital;
        row.appendChild(capitalCell);

        const actionsCell = document.createElement('td');
        const editButton = document.createElement('button');
        editButton.textContent = 'Редактировать';
        editButton.onclick = () => editCountry(country.id, nameCell, capitalCell, actionsCell);
        actionsCell.appendChild(editButton);

        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Удалить';
        deleteButton.onclick = () => deleteCountry(country.id, country.name);
        actionsCell.appendChild(deleteButton);

        row.appendChild(actionsCell);

        tableBody.appendChild(row);
    });
}

function deleteCountry(countryId, countryName) {
    fetch(`/api/countries/${countryId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                // Успешное удаление
                alert(`Страна "${countryName}" успешно удалена.`);
                loadCountries(); // Обновляем таблицу после удаления
            } else {
                alert(`Не удалось удалить страну "${countryName}". Возможно, это невозможно на данный момент.`);
            }
        })
        .catch(error => console.error(`Ошибка при удалении страны ${countryId}:`, error));
}

function editCountry(countryId, nameCell, capitalCell, actionsCell) {
    const currentName = nameCell.textContent;
    const currentCapital = capitalCell.textContent;

    const nameInput = document.createElement('input');
    nameCell.innerHTML = '';
    nameCell.appendChild(nameInput);
    nameInput.value = currentName;
    const capitalInput = document.createElement('input');
    capitalInput.value = currentCapital;
    capitalCell.innerHTML = '';
    capitalCell.appendChild(capitalInput)

    const saveButton = document.createElement('button');
    saveButton.textContent = 'Сохранить';
    saveButton.onclick = function() {
        fetch(`/api/countries/${countryId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: nameInput.value,
                capital: capitalInput.value,
            }),
        })
            .then(response => {
                if (response.ok) {
                    alert(`Страна "${nameInput.value}" успешно отредактирована.`);
                    loadCountries();
                } else {
                    alert(`Не удалось сохранить страну "${nameInput.value}". Возможно, это невозможно на данный момент.`);
                }
            })
            .catch(error => console.error('Ошибка при редактировании страны:', error));
    };

    const cancelButton = document.createElement('button');
    cancelButton.textContent = 'Отмена';
    cancelButton.onclick = function() {
        loadCountries()
    };
    actionsCell.childNodes.forEach(action =>{
        action.style.display = 'none'
    })
    actionsCell.appendChild(saveButton)
    actionsCell.appendChild(cancelButton)
    console.log(actionsCell.childElementCount)
}