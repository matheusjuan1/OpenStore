# CHANGELOG

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/spec/v2.0.0.html).

## [0.0.2] - 2025-10-27

### 🧹 Refatorado (Refactored)

- **Estrutura de Domínio:** 
  - O projeto foi refatorado para ter modelos (``models``) e repositórios (``repositories``) mais granulares e específicos por funcionalidade (ex: ``products``, ``cart``), melhorando o desacoplamento.
- **Consistência:** 
  - Pacotes e nomes de classes foram revisados para manter a consistência com os princípios de Arquitetura Limpa.

## [0.0.1] - 2025-10-23

Esta é a primeira versão Alpha do aplicativo OpenStore. Contém as funcionalidades mínimas viáveis para visualização de produtos e simulação de compra.

### ✨ Adicionado (Added)

- **Visualização de Produtos:**
  - Implementada a tela principal que exibe uma lista de produtos em um layout de grade (``GridLayout``) responsivo.
  - Exibição de mensagem de feedback quando os filtros de busca não retornam resultados.
  - Utilização da biblioteca Coil para carregamento e gerenciamento de imagens da internet.
- **Gerenciamento de Carrinho:**
  - Funcionalidade para adicionar e remover produtos do carrinho (em memória).
  - Atualização visual da quantidade de cada produto e Badge na barra de ferramentas.
- **Checkout e Pagamento:**
  - Adicionado o fluxo inicial de Checkout (``CheckoutActivity``).
  - Implementada a arquitetura de retorno de resultados (``ActivityResultLauncher``) para comunicar Sucesso/Falha entre Checkout e Produtos.
- **Navegação e Interação:**
  - ``Navigation Drawer`` para futuras seções.
  - Implementado ``FloatingActionButton`` (FAB) como atalho para o carrinho.
  - Adicionado um diálogo (``BottomSheetDialog``) de resumo do carrinho.
- **Filtros e Busca:**
  - Campo de busca para filtrar produtos por nome em tempo real.
  - Grupo de ``Chips`` para filtragem por categoria, garantindo que sempre haverá uma categoria selecionada.
- **Estrutura do Projeto:**
  - Arquitetura reativa com ``ViewModels``, ``StateFlow`` e ``DataResult`` (``sealed class``).
  - Implementação de injeção de dependência com Koin.
