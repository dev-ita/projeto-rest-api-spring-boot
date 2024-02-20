const salvarBtn = document.querySelector("#salvarBtn");
const resetBtn = document.querySelector("#resetBtn");
const buscarBtn = document.querySelector("#buscarBtn");
const deleteBtn = document.querySelector("#deleteBtn");

const idInput = document.querySelector("#id");
const nameInput = document.querySelector("#nome");
const idadeInput = document.querySelector("#idade");

salvarBtn.addEventListener("click", () => {
    const id = $("#id").val();
    const nome = $("#nome").val();
    const idade = $("#idade").val();

    $.ajax({
        method: "POST",
        url: "salvar",
        data: JSON.stringify({id, nome, idade}),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $("#id").val(data.id);
            alert("gravou com sucesso, id: " + data.id);
        },
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao salvar: " + xhr.responseText);
    });
});

resetBtn.addEventListener("click", () => {
    $("#id").val("");
    $("#nome").val("");
    $("#idade").val("");
});

buscarBtn.addEventListener("click", buscarUsuario);

// document.querySelector("#nome-usuario").addEventListener("keydown", buscarUsuario);

function buscarUsuario() {
    const nomeUsuario = document.querySelector("#nome-usuario").value;

    if (nomeUsuario != null && nomeUsuario.trim() !== "") {
        $.ajax({
            method: "GET",
            url: "buscarPorNome/" + nomeUsuario,
            success: function (data) {
                document.querySelector("#tabela-resultados > tbody").querySelectorAll("tr").forEach((item) => {
                    document.querySelector("#tabela-resultados > tbody").removeChild(item);
                });
                for (let usuario of data) {
                    const row = document.createElement("tr");
                    const id = document.createElement("td");
                    const nome = document.createElement("td");
                    const buttonVerTd = document.createElement("td");
                    const buttonVer = document.createElement("button");
                    const buttonDeleteTd = document.createElement("td");
                    const buttonDelete = document.createElement("button");

                    id.textContent = usuario.id;
                    nome.textContent = usuario.nome;

                    buttonVer.classList.add("btn", "btn-primary");
                    buttonVer.name = usuario.id;
                    buttonVer.textContent = "Ver";
                    buttonVer.addEventListener("click", function (ev) {
                        ev.preventDefault();
                        verUsuario(usuario.id);
                    });

                    buttonDelete.classList.add("btn", "btn-danger");
                    buttonDelete.name = usuario.id;
                    buttonDelete.textContent = "Deletar";
                    buttonDelete.addEventListener("click", function (ev) {
                        ev.preventDefault();
                        deletarUsuario(usuario.id);
                    });

                    buttonVerTd.append(buttonVer);
                    buttonDeleteTd.append(buttonDelete);

                    row.append(id, nome, buttonVerTd, buttonDeleteTd);

                    document.querySelector("#tabela-resultados > tbody").append(row);
                }
            },
        }).fail(function (xhr, status, errorThrown) {
            alert("Erro ao buscar usuarios: " + xhr.responseText);
        });
    } else {
        document.querySelector("#tabela-resultados > tbody").querySelectorAll("tr").forEach((item) => {
            document.querySelector("#tabela-resultados > tbody").removeChild(item);
        });
    }
}

deleteBtn.addEventListener("click", (ev) => {
    ev.preventDefault();
    if (idInput.value !== "") {
        deletarUsuario(idInput.value)
        idadeInput.value = "";
        idInput.value = "";
        nameInput.value = "";
    }
});

function deletarUsuario(id) {
    if (confirm(`Deseja deletar o usuario ${id}?`)) {
        $.ajax({
            method: "DELETE",
            url: "deletar/" + id,
            success: function (data) {
                document.querySelector(`button[name="${id}"]`).parentElement.parentElement.remove();
                return true;
            },
        }).fail(function (xhr, status, errorThrown) {
            alert("Erro ao deletar: " + xhr.responseText);
        });
    }
    return false;
}

function verUsuario(id) {
    $.ajax({
        method: "GET",
        url: "buscarUsuario",
        data: "id=" + id,
        success: function (data) {
            const {id, nome, idade} = data;
            idInput.value = id;
            nameInput.value = nome;
            idadeInput.value = idade;
            $("#pesquisarModal").modal("toggle");
        },
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar: " + xhr.responseText);
    });
}