export class Grid {

    constructor() {
        this.wordSelectMode = false;
        this.selectedItems = [];
        this.firstSelectedItem;
        this.gridArea = null;
        this.words = [];
        this.foundWords = [];
    }




    getCellsInRange(firstLetter, currentLetter) {
        let cellsInRange = [];
        if (firstLetter.x > currentLetter.x || firstLetter.y > currentLetter.y) {
            [currentLetter, firstLetter] = [firstLetter, currentLetter];
        }
        if (firstLetter.y === currentLetter.y) {
            for (let i = firstLetter.x; i <= currentLetter.x; i++) {
                cellsInRange.push(this.gridArea.querySelector(`td[data-x="${i}"][data-y="${currentLetter.y}"]`));
            }
        } else if (firstLetter.x === currentLetter.x) {
            for (let i = firstLetter.y; i <= currentLetter.y; i++) {
                cellsInRange.push(this.gridArea.querySelector(`td[data-x="${currentLetter.x}"][data-y="${i}"]`));
            }
        } else if (currentLetter.y - firstLetter.y === currentLetter.x - firstLetter.x) {
            let delta = currentLetter.y - firstLetter.y;
            for (let i = 0; i <= delta; i++) {
                cellsInRange.push(this.gridArea.querySelector(`td[data-x="${firstLetter.x + i}"][data-y="${firstLetter.y + i}"]`));
            }
        }

        return cellsInRange;
    }


    renderGrid(gridSize, wordGrid) {
        var gridArea = document.getElementsByClassName("grid-area")[0];
        if (gridArea.lastChild) {
            gridArea.removeChild(gridArea.lastChild);
        }
        this.gridArea = gridArea;
        var tbl = document.createElement("table");
        var tblBody = document.createElement("tbody");
        let index = 0;
        for (var i = 0; i < gridSize; i++) {
            var row = document.createElement("tr");

            for (var j = 0; j < gridSize; j++) {
                var cell = document.createElement("td");
                let letter = wordGrid[index++];
                var cellText = document.createTextNode(letter);
                cell.appendChild(cellText);
                cell.setAttribute("data-x", i);
                cell.setAttribute("data-y", j);
                cell.setAttribute("data-letter", letter);
                row.appendChild(cell);
            }

            tblBody.appendChild(row);
        }

        tbl.appendChild(tblBody);
        gridArea.appendChild(tbl);


        // Click Handlers
        tbl.addEventListener("mousedown", (event) => {
            this.wordSelectMode = true;
            const cell = event.target;
            let x = +cell.getAttribute("data-x");
            let y = +cell.getAttribute("data-y");
            let letter = cell.getAttribute("data-letter");
            this.firstSelectedItem = {
                x, y
            };
            
        });

        tbl.addEventListener("mousemove", (event) => {
            if (this.wordSelectMode) {
                const cell = event.target;
                let x = +cell.getAttribute("data-x");
                let y = +cell.getAttribute("data-y");
                let letter = cell.getAttribute("data-letter");
                this.selectedItems.forEach(cell => cell.classList.remove("selected"));
                this.selectedItems = this.getCellsInRange(this.firstSelectedItem, {x, y});
                this.selectedItems.forEach(cell => cell.classList.add("selected"));


            }
        });

        tbl.addEventListener("mouseup", (event) => {
            this.wordSelectMode = false;
            const selectedWord = this.selectedItems.reduce((word, cell) => word +=cell.getAttribute("data-letter"), '');
            const reversedSelectedWord = selectedWord.split("").reverse().join("");
            if (this.words.indexOf(selectedWord) !== -1) {
                this.foundWords.push(selectedWord);
                console.log("✅ Found:", selectedWord);
            } else if (this.words.indexOf(reversedSelectedWord) !== -1) {
                this.foundWords.push(reversedSelectedWord);
                console.log("✅ Found:", reversedSelectedWord);

            } else {
                console.log("❌ Not found, removing selection.");

                this.selectedItems.forEach(item => item.classList.remove("selected"));
            }
            console.log(this.foundWords)

            // Check if all words are found
            if (this.foundWords.length === this.words.length) {
                this.showSuccessMessage();
                this.startConfetti();
            }

            this.selectedItems = [];
        });
    }

    showSuccessMessage() {
        const popup = document.createElement("div");
        popup.innerHTML = "🎉 Congratulations! You found all words! 🎉";
        popup.style.position = "fixed";
        popup.style.top = "50%";
        popup.style.left = "50%";
        popup.style.transform = "translate(-50%, -50%)";
        popup.style.background = "#4CAF50";
        popup.style.color = "white";
        popup.style.padding = "20px";
        popup.style.fontSize = "24px";
        popup.style.borderRadius = "10px";
        popup.style.boxShadow = "0 4px 8px rgba(0, 0, 0, 0.2)";
        popup.style.zIndex = "1000";

        document.body.appendChild(popup);

        // Remove the popup after 3 seconds
        setTimeout(() => {
            popup.remove();
        }, 3000);
    }

// 🎊 Confetti effect
    startConfetti() {
        confetti({
            particleCount: 200,
            spread: 70,
            origin: { y: 0.6 }
        });
    }

}