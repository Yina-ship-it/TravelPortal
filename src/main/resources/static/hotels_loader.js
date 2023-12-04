let hotelsTableVisible = false;

function toggleHotelsTable() {
    if (!hotelsTableVisible) {
        loadHotels();
        document.getElementById('hotelsTable').style.display = 'table';
        document.getElementById('hotelsLoaderButton').textContent = 'Скрыть отели';
    } else {
        document.getElementById('hotelsTable').style.display = 'none';
        document.getElementById('hotelsLoaderButton').textContent = 'Загрузить отели';
    }

    hotelsTableVisible = !hotelsTableVisible;
}

function loadHotels() {
    fetch('/api/hotels/')
        .then(response => response.json())
        .then(data => {
            displayHotels(data);
        })
        .catch(error => console.error('Ошибка при загрузке отелей:', error));
}

function displayHotels(hotels) {
    const tableBody = document.querySelector('#hotelsTable tbody');
    tableBody.innerHTML = '';

    hotels.forEach(hotel => {
        const row = document.createElement('tr');

        const nameCell = document.createElement('td');
        nameCell.textContent = hotel.name;
        row.appendChild(nameCell);

        const countryCell = document.createElement('td');

        countryCell.textContent = hotel.countryName;
        row.appendChild(countryCell);

        const starsCell = document.createElement('td');
        starsCell.textContent = hotel.stars;
        row.appendChild(starsCell);

        const websiteCell = document.createElement('td');
        websiteCell.textContent = hotel.website;
        row.appendChild(websiteCell);

        const actionsCell = document.createElement('td');
        const editButton = document.createElement('button');
        editButton.textContent = 'Редактировать';
        actionsCell.appendChild(editButton);

        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Удалить';
        actionsCell.appendChild(deleteButton);

        row.appendChild(actionsCell);

        tableBody.appendChild(row);
    });
}