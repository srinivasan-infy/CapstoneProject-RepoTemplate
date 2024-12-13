package com.infy.utility;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.net.URL;

import com.infy.driverFactory.ConfigLoaderUtility;

public class APIUtility {

	private static final Logger logger = LoggerFactory.getLogger(APIUtility.class);
	private static final String username = ConfigLoaderUtility.getProperty("jiraURL").orElse("srinivasan");
	private static final String personalAccessToken = ConfigLoaderUtility.getProperty("jiraPAT")
																		 .orElse("ODcyMDMyODg1MjE5Ol/Oo9ZnBG3lnIL3q2ak28gTPts4");
	private static String jiraUrl = ConfigLoaderUtility.getProperty("jiraURL").orElse("http://localhost:6060/rest/api/2/issue/");
	
	public static void updateJiraIssue(String issueKey, boolean status, String Status, String scenarioName) {
		
		String optionId = status ? "10001" : "10000";
		String json = String.format("{\"fields\": {\"customfield_10200\": {\"id\": \"%s\"}}}", optionId);
	
		try {
			Response response = given().contentType(ContentType.JSON)
					.header("Authorization", "Bearer " + personalAccessToken)
					.body(json).when().put(jiraUrl + issueKey);

			int statusCode = response.getStatusCode();
			logger.info("Response Code: {}", statusCode);

			if (statusCode == 204) {
				logger.info("Successfully updated JIRA issue: {} with status: {} and scenario: {}", issueKey, Status,
						scenarioName);
			} else {
				logger.error("Error updating JIRA issue: {} with status: {}. Response: {}", issueKey, Status,
						response.getBody().asString());
			}
		} catch (Exception e) {
			logger.error("Error updating JIRA issue: {} with status: {}. Exception: {}", issueKey, Status,
					e.getMessage(), e);
		}
	}
	
	public static String sendGetRequestAndFetchKey(String baseUrl, String endpoint, String accountId, String key) {
		String url = baseUrl + endpoint + accountId;
		System.out.println(url);
		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.when().get(url).then().statusCode(200).extract().response();

		System.out.println("Response: ");
		response.prettyPrint();

		String value = response.jsonPath().getString(key);
		return value;

	}

	public static Response sendGetRequest(String baseUrl, String endpoint, String accountId) {
		String url = baseUrl + endpoint + accountId;
		System.out.println("GET Request URL: " + url);

		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.when().get(url).then().statusCode(200).extract().response();

		System.out.println("GET Response: ");
		response.prettyPrint();

		return response;
	}

	// Function to make a GET request with baseURL and endpoint
	public static String sendGETRequest(String baseURL, String endpoint) {
		RestAssured.baseURI = baseURL;

		Response response = RestAssured.given().header("Content-Type", "application/json").when().get(endpoint);

		if (response.getStatusCode() == 200) {
			return response.getBody().asString();
		} else {
			return "GET request failed. Response code: " + response.getStatusCode();
		}
	}

	public static Response sendGetRequestWithQueryParams(String baseUrl, String endpoint,
			Map<String, String> queryParams) {
		String url = baseUrl + endpoint;
		System.out.println("GET Request URL: " + url + " with query params: " + queryParams);

		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.queryParams(queryParams).when().get(url).then().statusCode(200).extract().response();

		System.out.println("GET Response: ");
		response.prettyPrint();

		return response;
	}

	public static Response sendGetRequestWithHeaders(String baseUrl, String endpoint, String accountId,
			Map<String, String> headers) {
		String url = baseUrl + endpoint + accountId;
		System.out.println("GET Request URL: " + url);

		Response response = given().headers(headers).when().get(url).then().statusCode(200).extract().response();

		System.out.println("GET Response: ");
		response.prettyPrint();

		return response;
	}

	public static void sendPostRequestAndVerifyResponse(String baseUrl, String accountId, String amount) {
		String url = baseUrl + "/services/bank/deposit";

		String expectedMessage = "Successfully deposited $" + amount + " to account #" + accountId;
		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.queryParam("accountId", accountId).queryParam("amount", amount).when().post(url).then().statusCode(200)
				.body(containsString(expectedMessage)).extract().response();
		System.out.println("Response: " + response.asString());
	}

	public static Response sendPostRequest(String baseUrl, String endpoint, Object body) {
		String url = baseUrl + endpoint;
		System.out.println("POST Request URL: " + url);

		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.body(body).when().post(url).then().statusCode(201)
				.extract().response();

		System.out.println("POST Response: ");
		response.prettyPrint();

		return response;
	}

	public static Response sendPostRequestWithHeaders(String baseUrl, String endpoint, Object body,
			Map<String, String> headers) {
		String url = baseUrl + endpoint;
		System.out.println("POST Request URL: " + url);

		Response response = given().headers(headers).body(body).when().post(url).then().statusCode(201)
				.extract().response();

		System.out.println("POST Response: ");
		response.prettyPrint();

		return response;
	}

	public static Response sendPostRequestWithQueryParams(String baseUrl, String endpoint, Object body,
			Map<String, String> queryParams) {
		String url = baseUrl + endpoint;
		System.out.println("POST Request URL: " + url + " with query params: " + queryParams);

		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.queryParams(queryParams).body(body).when().post(url).then().statusCode(201)
				.extract().response();

		System.out.println("POST Response: ");
		response.prettyPrint();

		return response;
	}

	public static Response sendPutRequest(String baseUrl, String endpoint, String accountId, Object body) {
		String url = baseUrl + endpoint + accountId;
		System.out.println("PUT Request URL: " + url);

		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.body(body).when().put(url).then().statusCode(200)
				.extract().response();

		System.out.println("PUT Response: ");
		response.prettyPrint();

		return response;
	}

	public static Response sendDeleteRequest(String baseUrl, String endpoint, String accountId) {
		String url = baseUrl + endpoint + accountId;
		System.out.println("DELETE Request URL: " + url);

		Response response = given().header("Accept", "application/json").header("Content-Type", "application/json")
				.when().delete(url).then().statusCode(204)
				.extract().response();

		System.out.println("DELETE Response: ");
		response.prettyPrint();

		return response;
	}

	public static Response getRequest(String url, Map<String, String> headers, Map<String, String> queryParams,
			ContentType contentType) {
		return given().relaxedHTTPSValidation().urlEncodingEnabled(false).headers(headers).queryParams(queryParams)
				.contentType(contentType).when().log().all().get(url).then().log().all().extract().response();
	}
}