# Étape 1 - Utilisation d'une image avec Maven et Java 21
FROM maven:3.9.9-eclipse-temurin-21

# Étape 2: Création d'un dossier pour notre projet
WORKDIR /app

# Étape 3: Copie des fichiers nécessaires
COPY pom.xml .
COPY src src/

# Étape 4: Construction de l'application
RUN mvn clean package -DskipTests

# Étape 5: Commande de démarrage
CMD ["java", "-jar", "target/sharebook-0.0.1-SNAPSHOT.jar"]
