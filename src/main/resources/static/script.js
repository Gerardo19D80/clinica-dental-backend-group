document.addEventListener('DOMContentLoaded', () => {
    const odontologosList = document.getElementById('odontologos-list');

    fetch("http://localhost:8085/odontologos")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {

            data.forEach(odontologo => {

                const odontologoElement = document.createElement('div');
                odontologoElement.innerHTML = `
                    <p>Nombre: ${odontologo.nombre}</p>
                    <p>Apellido: ${odontologo.apellido}</p>
                    <p>Matrícula: ${odontologo.matricula}</p>
                `;
                odontologosList.appendChild(odontologoElement);
            });
        })
        .catch(error => {
            console.error('Error fetching data:', error);

            odontologosList.innerHTML = '<p>Error al cargar los datos de los odontólogos.</p>';
        });
});