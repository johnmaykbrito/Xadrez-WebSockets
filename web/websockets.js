var conn, BRANCA = 0, PRETA = 1, origem = null, corDaVez = BRANCA;
function Connection() {
    this.ws = null;
    var _self = this;
    this.onOpen = function () {
        _self.writeResponse("Aguardando jogador, my brother...");
    };
    this.onMessage = function (evt) {
        var data = JSON.parse(evt.data);
        if (data.type === 0) {
            cor = data.color;
            corDaVez = data.turn;
            turnMessage();
        } else if (data.type === 1) {
            alert(data.type);
            corDaVez = data.turn;
            // coord beginning
            var begin = data.begin;
            // coord end
            var end = data.end;
            movePiece(begin, end);
            turnMessage();
        } else if (data.type === 2) {
            alert(data.type);
            _self.writeResponse(data.message);
        } else if (data.type === 3) {
            alert(data.type);
            _self.writeResponse("Connection closed.");
            this.ws.close();
        } else if (data.type === 4) {
            fillBoard(data.board);
            corDaVez = data.turn;
        } else {
            //console.log(data);
        }
        //setEvents();
    };
    this.onClose = function () {
        _self.writeResponse("Connection closed.");
    };
    this.onError = function (evt) {
        _self.writeResponse("error: " + evt.data);
    };
    this.writeResponse = function (text) {
        var messages = document.getElementById("results");
        messages.innerHTML = text;
    };
    this.openConnection = function () {
        var wsUri = "ws://" + document.location.host + document.location.pathname + "chess";
        this.ws = new WebSocket(wsUri);
        this.ws.binaryType = "arraybuffer";
        this.ws.onopen = this.onOpen;
        this.ws.onmessage = this.onMessage;
        this.ws.onerror = this.onError;
        this.ws.onclose = this.onClose;

    };
}

function fillBoard(board) {
    var table = document.getElementsByTagName("table")[0];
//    console.log(table);
    board.forEach(function (row, rowIndex) {
        row.forEach(function (col, colIndex) {
            var cell = table.rows[rowIndex].cells[colIndex];
//            console.log(cell);
//            
            cell.innerHTML =
                    (col === 0 ? '<img src="imagens/Rainha-Preta.svg" alt="">' :
                            (col === 1 ? '<img src="imagens/Rei-Preto.svg" alt="">' :
                                    (col === 2 ? '<img src="imagens/Bispo-Preto.svg" alt="">' :
                                            (col === 3 ? '<img src="imagens/Cavalo-Preto.svg" alt="">' :
                                                    (col === 4 ? '<img src="imagens/Torre-Preta.svg" alt="">' :
                                                            (col === 5 ? '<img src="imagens/Peao-Preto.svg" alt="">' :
                                                                    (col === 6 ? '<img src="imagens/Rainha-Branca.svg" alt="">' :
                                                                            (col === 7 ? '<img src="imagens/Rei-Branco.svg" alt="">' :
                                                                                    (col === 8 ? '<img src="imagens/Bispo-Branco.svg" alt="">' :
                                                                                            (col === 9 ? '<img src="imagens/Cavalo-Branco.svg" alt="">' :
                                                                                                    (col === 10 ? '<img src="imagens/Torre-Branca.svg" alt="">' :
                                                                                                            (col === 11 ? '<img src="imagens/Peao-Branco.svg" alt="">' : "")
                                                                                                            )))))))))));

            var x = cell.firstChild;
            if (x) {
                //console.log(x);
                x.draggable = true;
                x.ondragstart = drag;
            }
            cell.ondragover = allowDrop;
            cell.ondrop = drop;
        });
    });
}

function drag(ev) {
    origem = this;
    ev.dataTransfer.effectAllowed = 'move';
    //get coordinates
    //ev.dataTransfer.setData("text/plain", "[" + coordinates(this.parentNode) + "]");
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drop(ev) {
    ev.preventDefault();
    var cell = origem.parentNode;
    // origem
    console.log('[' + cell.parentNode.rowIndex + ', ' + cell.cellIndex + ']');

    //console.log(this);

    // destino
    console.log('[' + this.parentNode.rowIndex + ', ' + this.cellIndex + ']');


    console.log(JSON.stringify({ox: cell.parentNode.rowIndex, oy: cell.cellIndex, dx: this.parentNode.rowIndex, dy: this.cellIndex}));

    conn.ws.send(JSON.stringify({ox: cell.parentNode.rowIndex, oy: cell.cellIndex, dx: this.parentNode.rowIndex, dy: this.cellIndex}));
}

function turnMessage() {
    if (cor === corDaVez) {
        conn.writeResponse("Its your turn.");
    } else {
        conn.writeResponse("Wait for your turn.");
    }
}

onload = function () {
    conn = new Connection();
    conn.openConnection();
};
