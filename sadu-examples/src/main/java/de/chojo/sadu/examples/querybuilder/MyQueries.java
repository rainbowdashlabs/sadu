package de.chojo.sadu.examples.querybuilder;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.wrapper.stage.UpdateResult;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

// Our class will extend the QueryFactory.
// This allows us to simply create preconfigured builder.
public class MyQueries extends QueryFactory {

    /**
     * Create a new queries object.
     *
     * @param dataSource data source used to query data from a database
     */
    public MyQueries(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Retrieve a result by id.
     *
     * @param id id to retrieve
     * @return An optional holding a result if found.
     */
    public CompletableFuture<Optional<Result>> getResult(int id) {
        // We want to have a class of type Result.
        return builder(Result.class)
                // We define our query
                .query("SELECT result FROM results WHERE id = ?")
                // We set the first parameter. No need to define the index.
                .parameter(stmt -> stmt.setInt(id))
                // We map our current row to a result.
                .readRow(rs -> new Result(rs.getString("result")))
                // We retrieve only the first result we get.
                .first();
    }

    /**
     * Retrieve a list of all results in the result table.
     *
     * @return list of results
     */
    public CompletableFuture<List<Result>> getResults() {
        // We want to have a class of type Result.
        return builder(Result.class)
                // We define our query
                .query("SELECT result FROM results")
                // We skip the parameter assignment
                .emptyParams()
                // We map our current row to a result.
                .readRow(rs -> new Result(rs.getString("result")))
                // We retrieve only the first result we get.
                .all();
    }

    /**
     * Delete a result.
     *
     * @param id the id to delete
     * @return true if result was present and got deleted
     */
    public CompletableFuture<Boolean> deleteResult(int id) {
        // We want to delete. We leave the expected class empty.
        return builder()
                // We define our query
                .query("DELETE FROM results WHERE id = ?")
                // We set the first parameter. No need to define the index.
                .parameter(stmt -> stmt.setInt(id))
                // We say that we want to execute a deletion
                .delete()
                // We execute the query asynchronously
                .send()
                // We check if a row was changed
                .thenApply(UpdateResult::changed);
    }

    /**
     * Delete a result.
     *
     * @param id the id to delete
     * @return true if result was present and got updated
     */
    public CompletableFuture<Boolean> updateResult(int id, String newValue) {
        // We want to update. We leave the expected class empty.
        return builder()
                // We define our query
                .query("UPDATE results SET result = ? WHERE id = ?")
                // The param builder will set the parameters in the order you define them
                .parameter(stmt -> stmt.setString(newValue).setInt(id))
                // We say that we want to execute a deletion
                .update()
                // We execute the query asynchronously
                .send()
                // We check if a row was changed
                .thenApply(UpdateResult::changed);
    }

    /**
     * Delete a result.
     *
     * @param result the result to add
     * @return returns the id of the new result
     */
    public CompletableFuture<Optional<Long>> addResult(String result) {
        // We want to insert. We leave the expected class empty.
        return builder()
                // We define our query
                .query("INSERT INTO results(result) VALUES(?)")
                // We set the first parameter. No need to define the index.
                .parameter(stmt -> stmt.setString(result))
                // We say that we want to execute a insert
                .insert()
                // We execute the query asynchronously and get the created key.
                .key();
    }

    private class Result{
        private final String result;

        private Result(String result) {
            this.result = result;
        }
    }
}
