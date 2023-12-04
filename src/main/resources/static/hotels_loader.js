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
        editButton.onclick = () => editHotel(hotel.id, nameCell, countryCell, starsCell, websiteCell, actionsCell);
        actionsCell.appendChild(editButton);

        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Удалить';
        deleteButton.onclick = () => deleteHotel(hotel.id, hotel.name);
        actionsCell.appendChild(deleteButton);

        row.appendChild(actionsCell);

        tableBody.appendChild(row);
    });
}

function deleteHotel(hotelId, hotelName) {
    fetch(`/api/hotels/${hotelId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert(`Отель "${hotelName}" успешно удален.`);
                loadHotels();
            } else {
                alert(`Не удалось удалить отель "${hotelName}". Возможно, это невозможно на данный момент.`);
            }
        })
        .catch(error => console.error(`Ошибка при удалении отеля ${hotelId}:`, error));
}

function editHotel(hotelId, nameCell, countryCell, starsCell, websiteCell, actionsCell) {
    const currentName = nameCell.textContent;
    const currentCountryName = countryCell.textContent;
    const currentStars = starsCell.textContent;
    const currentWebsite = websiteCell.textContent;

    const nameInput = document.createElement('input');
    nameCell.innerHTML = '';
    nameCell.appendChild(nameInput);
    nameInput.value = currentName;

    const countryInput = document.createElement('select');
    fetch('/api/countries/')
        .then(response => response.json())
        .then(countries => {
            countries.forEach(country => {
                const option = document.createElement('option');
                option.value = country.id;
                option.text = country.name;
                if (country.name === currentCountryName) {
                    option.selected = true
                    countryInput.value = country.id;
                }
                countryInput.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading countries:', error));
    countryCell.innerHTML = '';
    countryCell.appendChild(countryInput);

    const starsInput = document.createElement('input');
    starsInput.setAttribute('type', 'number');
    starsInput.setAttribute('min', '1');
    starsInput.setAttribute('max', '5');
    starsCell.innerHTML = '';
    starsCell.appendChild(starsInput);
    starsInput.value = currentStars;

    const websiteInput = document.createElement('input');
    websiteInput.setAttribute('type', 'url');
    websiteCell.innerHTML = '';
    websiteCell.appendChild(websiteInput);
    websiteInput.value = currentWebsite;

    const saveButton = document.createElement('button');
    saveButton.textContent = 'Сохранить';
    saveButton.onclick = () => {
        fetch(`/api/hotels/${hotelId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: nameInput.value,
                countryId: countryInput.value,
                stars: starsInput.value,
                website: websiteInput.value
            }),
        })
            .then(response => {
                if (response.ok) {
                    alert(`Отель "${nameInput.value}" успешно отредактирована.`);
                    loadHotels();
                } else {
                    alert(`Не удалось отредактировать отель "${nameInput.value}". Возможно, это невозможно на данный момент.`);
                }
            })
            .then(updatedHotel => {
                nameCell.textContent = updatedHotel.name;
                countryCell.textContent = updatedHotel.countryName;
                starsCell.textContent = updatedHotel.stars;
                websiteCell.textContent = updatedHotel.website;
            })
            .catch(error => console.error('Ошибка при сохранении отеля:', error));
    };

    const cancelButton = document.createElement('button');
    cancelButton.textContent = 'Отмена';
    cancelButton.type = 'button';
    cancelButton.onclick = () => loadHotels();

    actionsCell.childNodes.forEach(action => action.style.display = 'none')
    actionsCell.appendChild(saveButton);
    actionsCell.appendChild(cancelButton);
}