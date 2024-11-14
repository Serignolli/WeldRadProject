# app.py

import os
from flask import Flask, request, jsonify
from analisys.analisys import analisar_imagem
from PIL import Image
import io

app = Flask(__name__)

@app.route('/api/analisys', methods=['POST'])
def analisar_radiografia():
    data = request.json
    if 'image_path' not in data:
        return jsonify({'erro': 'Nenhum caminho de imagem foi enviado.'}), 400

    image_path = data['image_path']

    if not os.path.exists(image_path):
        return jsonify({'erro': 'O arquivo de imagem não foi encontrado. - '+ image_path}), 404

    try:
        with Image.open(image_path) as imagem:
            resultado = analisar_imagem(imagem)
        return jsonify({'resultado': resultado})
    except Exception as e:
        return jsonify({'erro': f'Erro ao processar a imagem: {str(e)}'}), 500

if __name__ == '__main__':
    app.run(debug=True)
