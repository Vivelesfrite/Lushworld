# Lushworld

**Lushworld** est un plugin Minecraft permettant de gérer facilement des mondes personnalisés sur votre serveur. Avec Lushworld, vous pouvez :

- Créer, supprimer et régénérer des mondes.
- Lier des mondes via des portails personnalisés.
- Téléporter des joueurs à leur dernière position connue ou au spawn du monde.
- Gérer la configuration des mondes avec des fonctionnalités étendues.

## Fonctionnalités

- **Création de mondes** : Créez facilement des mondes personnalisés avec la commande `/createmonde`.
- **Téléportation de mondes** : Téléportez-vous ou d'autres joueurs entre des mondes avec `/tpmonde`.
- **Regénération de mondes** : Régénérez des mondes en utilisant la commande `/regenmonde`.
- **Suppression de mondes** : Supprimez des mondes avec `/deletemonde`.
- **Liens entre mondes** : Utilisez des portails pour relier des mondes.

## Installation

1. Téléchargez le plugin depuis GitHub.
2. Placez le fichier `.jar` dans le dossier `plugins` de votre serveur Minecraft.
3. Redémarrez votre serveur.

## Commandes

- `/createmonde [nom] <type>` : Crée un monde personnalisé. **/!\ Le nom du monde Nether doit être `NOMDUMONDE_nether`
  (pour un monde Nether) ou `NOMDUMONDE_end` (pour un monde End)**, et il faut préciser le type du monde après.
- `/tpmonde [joueur] [monde]` : Téléporte un joueur dans un monde spécifique.
- `/deletemonde [nom]` : Supprime un monde.
- `/regenmonde [nom]` : Régénère un monde.
- `/linkmonde [monde1] <nether/end>` : Crée un lien entre deux mondes. Le plugin va rechercher un fichier nommé `exemple_nether` (pour un monde Nether) ou `exemple_end` (pour un monde End).

## Contributions

Les contributions sont les bienvenues ! Si vous souhaitez améliorer ce plugin, veuillez ouvrir une issue ou une pull request.

## License

Ce projet est sous licence MIT.
