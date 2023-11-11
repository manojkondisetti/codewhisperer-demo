package com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo;

import com.amazon.aws.authruntimeclient.internal.common.collect.ImmutableMap;
import com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.exception.CodeWhispererPictureException;
import com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.model.CodeWhispererPictureMetadata;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

import javax.inject.Inject;

public class CodeWhispererDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeWhispererDB.class);
    private final DynamoDbEnhancedClient dynamodbClient;
    private final DynamoDbTable<CodeWhispererPictureMetadata> table;
    private final String TABLE_NAME = "CodeWhispererDemo";

    @Inject
    public CodeWhispererDB(final DynamoDbEnhancedClient dynamodbClient) {
        this.dynamodbClient = Validate.notNull(dynamodbClient);
        table = dynamodbClient.table(TABLE_NAME, TableSchema.fromBean(CodeWhispererPictureMetadata.class));
    }

    /**
     * Stores the picture metadata in the desired table.
     */
    public  void storePictureMetadata(final CodeWhispererPictureMetadata metadata) {
        try {
            ImmutableMap<String, String> keyMap = ImmutableMap.of("#partitionKey", metadata.getPictureId());
            final Expression expression = Expression.builder()
                    .expressionNames(keyMap)
                    .expression("attribute_not_exists(#partitionKey)")
                    .build();

            final PutItemEnhancedRequest<CodeWhispererPictureMetadata> request = PutItemEnhancedRequest.builder(CodeWhispererPictureMetadata.class)
                    .conditionExpression(expression)
                    .item(metadata)
                    .build();

            table.putItem(request);
        } catch (Exception exception) {
            String message = String.format("Exception occurred while storing metadata for pictureId: %s", metadata.getPictureId());
            LOGGER.error(message, exception);
            throw new CodeWhispererPictureException(message, exception);        }
    }

    /**
     * Get the metadata for a given pictureId.
     */
    public CodeWhispererPictureMetadata getPictureMetadata(final String pictureId) {
        try {
            final GetItemEnhancedRequest request = GetItemEnhancedRequest.builder()
                    .consistentRead(true)
                    .key(Key.builder()
                            .partitionValue(pictureId)
                            .build())
                    .build();

            CodeWhispererPictureMetadata metadata = table.getItem(request);
            return metadata;
        } catch (Exception exception) {
            String message = String.format("Exception occurred while getting metadata for pictureId: %s", pictureId);
            LOGGER.error(message, exception);
            throw new CodeWhispererPictureException(message, exception);
        }
    }
}
