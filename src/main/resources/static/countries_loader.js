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