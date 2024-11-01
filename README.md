
# Azure Client Credentials Flow mit Java und IntelliJ

Dieses Projekt zeigt, wie der Client-Credentials-Flow von Microsoft Entra ID (ehemals Azure Active Directory) genutzt werden kann, um ein Access Token für den Zugriff auf geschützte Ressourcen in Azure zu erhalten. Der Code ist in Java geschrieben und verwendet die Microsoft Authentication Library (MSAL) für Java, um sich sicher zu authentifizieren.

## Voraussetzungen

1. **Java Development Kit (JDK)**: Stelle sicher, dass du JDK 8 oder höher installiert hast.
2. **IntelliJ IDEA**: Dieses Projekt wurde für die Nutzung in IntelliJ optimiert.
3. **Azure App-Registrierung**: Du musst eine App-Registrierung in Microsoft Entra ID eingerichtet haben. Hier erhältst du die Client-ID, den Client-Secret-Wert und die Tenant-ID.

## Einrichten des Projekts

1. **Klonen oder Erstellen des Projekts**: Klone dieses Repository oder erstelle ein neues Projekt in IntelliJ.
2. **MSAL für Java als Abhängigkeit hinzufügen**:
   - Wenn du **Maven** nutzt, öffne die `pom.xml` und füge Folgendes hinzu:
     ```xml
     <dependency>
         <groupId>com.microsoft.azure</groupId>
         <artifactId>msal4j</artifactId>
         <version>1.11.0</version>
     </dependency>
     ```
   - Wenn du **Gradle** nutzt, füge in der `build.gradle` die folgende Zeile hinzu:
     ```groovy
     implementation 'com.microsoft.azure:msal4j:1.11.0'
     ```

3. **Projekt synchronisieren**: Stelle sicher, dass IntelliJ die Abhängigkeiten synchronisiert, sodass die MSAL-Bibliothek für Java heruntergeladen wird.

## Konfiguration

Öffne die `AzureAuthExample.java`-Datei und ersetze die folgenden Platzhalter:

- `DEINE_CLIENT_ID`: Die Client-ID deiner Anwendung aus der App-Registrierung in Microsoft Entra ID.
- `DEIN_CLIENT_SECRET`: Der Client-Secret-Wert, den du für die App erstellt hast.
- `DEINE_TENANT_ID`: Die Tenant-ID deiner Azure AD-Instanz (findest du in den Azure AD-Informationen oder in der App-Registrierung).

Wenn du diese Werte ersetzt hast, bist du bereit, den Client-Credentials-Flow auszuführen.

## Beispiel-Code

Hier ist der Kerncode, der den Client-Credentials-Flow ausführt und das Access Token abruft:

```java
import com.microsoft.aad.msal4j.*;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AzureAuthExample {
    private static final String CLIENT_ID = "DEINE_CLIENT_ID";
    private static final String CLIENT_SECRET = "DEIN_CLIENT_SECRET";
    private static final String TENANT_ID = "DEINE_TENANT_ID";
    private static final String AUTHORITY = "https://login.microsoftonline.com/" + TENANT_ID;

    public static void main(String[] args) {
        try {
            IClientCredential credential = ClientCredentialFactory.createFromSecret(CLIENT_SECRET);
            ConfidentialClientApplication app = ConfidentialClientApplication.builder(CLIENT_ID, credential)
                    .authority(AUTHORITY)
                    .build();

            Set<String> scopes = Collections.singleton("https://graph.microsoft.com/.default");
            ClientCredentialParameters parameters = ClientCredentialParameters.builder(scopes).build();

            CompletableFuture<IAuthenticationResult> future = app.acquireToken(parameters);
            IAuthenticationResult result = future.join();

            System.out.println("Access Token: " + result.accessToken());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Access Token und API-Aufrufe

Das Access Token, das du über diesen Flow erhältst, ermöglicht deiner Anwendung, auf geschützte Ressourcen in Azure zuzugreifen. Im Beispiel oben wird der Scope `https://graph.microsoft.com/.default` verwendet, um Zugriff auf Microsoft Graph API zu erhalten.

### Zugriff auf mehrere Komponenten

Um auf andere Azure-Komponenten zuzugreifen, musst du die `scopes`-Variable anpassen. Hier sind ein paar Beispiele:

- **Microsoft Graph API**: Verwende den Scope `https://graph.microsoft.com/.default`.
- **Azure Key Vault**: Verwende den Scope `https://vault.azure.net/.default`.
- **Azure Storage**: Verwende den Scope `https://storage.azure.com/.default`.

Du kannst mehrere Scopes gleichzeitig anfordern, indem du sie der `scopes`-Menge hinzufügst, zum Beispiel:

```java
Set<String> scopes = new HashSet<>(Arrays.asList("https://graph.microsoft.com/.default", "https://vault.azure.net/.default"));
```

### Hinweise zu Scopes und Berechtigungen

Stelle sicher, dass du in der App-Registrierung die erforderlichen Berechtigungen für jeden Service erteilt hast. Gehe dazu in Azure-Portal zur App-Registrierung, wähle **API-Berechtigungen** und füge die entsprechenden Berechtigungen hinzu. 

## Ausführen des Programms

Um das Programm auszuführen, befolge diese Schritte:

1. **IntelliJ öffnen und Projekt importieren**: Öffne IntelliJ und lade das Projekt. Stelle sicher, dass alle Abhängigkeiten korrekt geladen und synchronisiert wurden.
2. **Java-Klasse starten**: Navigiere zur Datei `AzureAuthExample.java` und klicke auf das grüne "Run"-Symbol neben der `main`-Methode oder nutze den Shortcut `Shift + F10` (Standard in IntelliJ).
3. **Ergebnisse überprüfen**: Nach dem Start des Programms wird das Access Token im Konsolenfenster von IntelliJ ausgegeben. Dieses Token kann verwendet werden, um auf die gewünschten Azure-Ressourcen zuzugreifen.

### Wichtige Hinweise

- **Client-ID, Client-Secret und Tenant-ID prüfen**: Stelle sicher, dass diese Werte korrekt im Code hinterlegt sind.
- **API-Berechtigungen**: Wenn du Fehler wie `401 Unauthorized` oder `403 Forbidden` siehst, überprüfe die Berechtigungen für deine App-Registrierung im Azure-Portal. Alle benötigten Berechtigungen sollten der App-Registrierung hinzugefügt und (falls notwendig) genehmigt werden.

Sollten weitere Fehler auftreten, überprüfe die detaillierte Fehlermeldung in der Konsole, um die genaue Ursache zu identifizieren und Anpassungen vorzunehmen.
