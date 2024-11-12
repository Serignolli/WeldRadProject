import numpy as np
from tensorflow.keras.models import load_model

model = load_model('./analisys/best_model.keras')

#Analisa o patch da imagem
def analisar_patch(patch):

    #Pré-processamento
    patch = patch.resize((256, 256))
    patch = patch.convert('RGB')
    patch_array = np.array(patch) / 255.0
    patch_array = np.expand_dims(patch_array, axis=0)

    predict = model.predict(patch_array)

    if predict[0] > 0.5:
        return "Com_Defeito"
    else:
        return "Sem_Defeito"

#Analisa a imagem de acordo com o resultado de cada patch
def analisar_imagem(imagem):

    largura, altura = imagem.size
    tamanho_patch = 256

    resultados = {"Com_Defeito": 0, "Sem_Defeito": 0}

    # Dividir a imagem em patches e analisar cada patch
    for y in range(0, altura, tamanho_patch):
        for x in range(0, largura, tamanho_patch):
            # Recortar o patch
            box = (x, y, x + tamanho_patch, y + tamanho_patch)
            patch = imagem.crop(box)

            # Analisar o patch e contar os resultados
            resultado = analisar_patch(patch)
            resultados[resultado] += 1

    # Exibir os resultados
    total_patches = resultados["Com_Defeito"] + resultados["Sem_Defeito"]
    print(f"Analisado {total_patches} patches.")
    print(f"Com Defeito: {resultados['Com_Defeito']}")
    print(f"Sem Defeito: {resultados['Sem_Defeito']}")

    #Calculo mais ou menos pra determinar se tem defeito ou não
    if resultados["Com_Defeito"] > resultados["Sem_Defeito"]:
        print("Imagem classificada como: Com Defeito")
        return "Com Defeito"
    else:
        print("Imagem classificada como: Sem Defeito")
        return "Sem Defeito"
