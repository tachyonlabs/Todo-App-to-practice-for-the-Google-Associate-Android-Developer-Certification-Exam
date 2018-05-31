package com.tachyonlabs.practicetodoapp;

import com.tachyonlabs.practicetodoapp.models.TodoTask;

import org.junit.Assert;
import org.junit.Test;

public class TodoTaskUnitTest {
    TodoTask mTodoTask = new TodoTask();
    private final static String TEST_DESCRIPTION = "Test description";
    private final static int TEST_PRIORITY = TodoTask.LOW_PRIORITY;
    private final static long TEST_DUE_DATE = 1527750000000L; // May 31, 2018, when I added these tests :-)
    private final static int TEST_ID = 2018;
    private final static int TEST_COMPLETED = TodoTask.TASK_COMPLETED;

    @Test
    public void testDescription() {
        mTodoTask.setDescription(TEST_DESCRIPTION);
        Assert.assertEquals(mTodoTask.getDescription(), TEST_DESCRIPTION);
    }

    @Test
    public void testPriority() {
        mTodoTask.setPriority(TEST_PRIORITY);
        Assert.assertEquals(mTodoTask.getPriority(), TEST_PRIORITY);
    }

    @Test
    public void testDueDate() {
        mTodoTask.setDueDate(TEST_DUE_DATE);
        Assert.assertEquals(mTodoTask.getDueDate(), TEST_DUE_DATE);
    }

    @Test
    public void testId() {
        mTodoTask.setId(TEST_ID);
        Assert.assertEquals(mTodoTask.getId(), TEST_ID);
    }

    @Test
    public void testCompleted() {
        mTodoTask.setCompleted(TEST_COMPLETED);
        Assert.assertEquals(mTodoTask.getCompleted(), TEST_COMPLETED);
    }
}
