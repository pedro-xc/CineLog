# CineLog

Diário pessoal de filmes e séries assistidos. Busque títulos, veja detalhes e salve com sua nota, comentário e status (assistido / quero assistir). Acompanhe estatísticas simples do que já foi assistido — tudo isso funcionando offline depois de salvo.

## Funcionalidades

- **Buscar**: pesquise filmes na API do TMDb com poster, título e ano.
- **Detalhes**: veja sinopse, nota da comunidade e salve sua própria avaliação (nota, comentário e status).
- **Diário**: lista de tudo que você já salvou, lida direto do banco local — sem depender de internet.
- **Estatísticas**: total assistido, total na lista "quero assistir" e sua nota média pessoal.
- Tratamento de erro de rede e indicadores de carregamento nas telas que dependem da API.

## Stack técnica

- Kotlin, sem Jetpack Compose (XML layouts + View Binding)
- Arquitetura MVVM com camada de Repository
- **Retrofit** + **Gson** + **OkHttp** para consumir a API do [TMDb](https://www.themoviedb.org/documentation/api)
- **Room** para persistência local
- **Glide** para carregamento de imagens
- **Navigation Component** com Fragments e `BottomNavigationView`
- **ViewModel** + **StateFlow** (Coroutines) para gerenciamento de estado

## Estrutura do projeto

```
app/src/main/java/com/pedro/cinelog/
├── data/
│   ├── local/        # Room: Entity, Dao, Database, Converters
│   ├── remote/        # Retrofit: ApiService, DTOs, interceptor de API key
│   └── repository/    # MovieRepository (unifica local + remoto)
├── ui/
│   ├── search/         # Busca de filmes
│   ├── detail/          # Detalhes + salvar no diário
│   ├── diary/          # Diário salvo localmente
│   └── stats/           # Estatísticas
└── MainActivity.kt
```

## Configuração

O app precisa de uma chave da API do TMDb para funcionar.

1. Crie uma conta gratuita em [themoviedb.org](https://www.themoviedb.org/signup).
2. Gere uma **API Key (v3 auth)** em [Configurações → API](https://www.themoviedb.org/settings/api).
3. Na raiz do projeto, edite (ou crie) o arquivo `local.properties` e adicione:
   ```properties
   TMDB_API_KEY=sua_chave_aqui
   ```
4. Sincronize/recompile o projeto no Android Studio.

## Rodando o projeto

Abra a pasta no Android Studio, aguarde o sync do Gradle e rode o módulo `app` em um emulador ou dispositivo físico (mínimo SDK 34).
