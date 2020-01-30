package com.example.voicerecorder

//import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
//import androidx.test.runner.AndroidJUnit4


//import com.google.common.truth.Truth.assertThat

import android.app.Activity
import android.content.Context
import android.media.MediaRecorder
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


//import androidx.test.espresso.intent.Intents.intended

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getTargetContext()
//        assertEquals("com.example.voicerecorder", appContext.packageName)
//    }
//}


fun printRecordDir(context: Context) {

    val recordsDir = File(getRecordingDir(context.filesDir.absolutePath))

    recordsDir.walkTopDown().sortedByDescending { it.name }.forEach {
        println(it.name)
    }
}


@RunWith(AndroidJUnit4::class)
class CommonFunctionsTest {

    private val workContext = ApplicationProvider.getApplicationContext<Context>()!!

    @Test
    fun getRecordingDir_ShouldReturnCorrectDir() {
        val resultString = "/data/user/0/com.example.voicerecorder/files/records"
        val filesPath = workContext.filesDir.absolutePath
        assertEquals(getRecordingDir(filesPath), resultString)
    }
}

@RunWith(AndroidJUnit4::class)
class ChangeActivity {

    @get:Rule
    var mainActivityRule = ActivityTestRule(MainActivity::class.java)
//    @Before


    @Test
    fun changeActivityByClick_ListActivity_Is_Displayed () {

        onView(withId(R.id.view_my_list_button)).perform(click())

        assertEquals(getActivityInstance().javaClass, ListActivity::class.java)
    }


    @Test
    fun changeActivityBySwipe_ListActivity_Is_Displayed () {

        assertEquals(getActivityInstance().javaClass, mainActivityRule.activity.javaClass)

        onView(withId(R.id.constraint_layout_id)).perform(swipeLeft())

        Thread.sleep(500)
        assertEquals(getActivityInstance().javaClass, ListActivity::class.java)
    }


    private fun getActivityInstance() : Activity {

        val currentActivity = arrayOf<Activity?>(null)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val resumedActivities: Collection<*> =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)

            if (resumedActivities.iterator().hasNext()) {
                currentActivity[0] = resumedActivities.iterator().next() as Activity?
            }
        }

        return currentActivity[0]!!
    }
}


@RunWith(AndroidJUnit4::class)
class RecordFunctionality {

    val RECORD_DIR = "/data/user/0/com.example.voicerecorder/files/records"
    var fakeRecorderState = "NA"
    var fileIndex = 1

    private val startRecButton = onView(withId(R.id.start_record))
    private val stopRecButton = onView(withId(R.id.stop_record))

    fun startRecord() {
        startRecButton.perform(click())
    }

    fun stopRecord() {
        stopRecButton.perform(click())
    }

    fun getTestMediaRecorder(): MediaRecorder {

        class FakeMediaRecorder : MediaRecorder() {

            override fun start() {
                fakeRecorderState = "Started"
                println("\n\t\nFAKE RECORDING STARTED\n")
            }

            override fun stop() {
                fakeRecorderState = "Stopped"
                println("\n\t\nSTOP FAKE RECORDING\n")
            }
        }

        return FakeMediaRecorder()
    }

    fun getFakeFile() : File {
        return File(RECORD_DIR + File.separator + "test.mp3")
    }

    @get:Rule
    var mainActivityRule = ActivityTestRule(MainActivity::class.java)
//    var mainActivityRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun recordWithFakeRecorder_ShouldDoFakeFunction () {
        val fakeMediaRecorder = getTestMediaRecorder()
        val appCollection = mainActivityRule.activity.appCollection

        mainActivityRule.activity.startRecord(appCollection.getFileInst(), fakeMediaRecorder)
        assertEquals("Started", fakeRecorderState)

        mainActivityRule.activity.stopRecording()
        assertEquals("Stopped", fakeRecorderState)

    }


    @Test
    fun recordWithFileInjection_ShouldCreateTestRecord () {

        val currContext = mainActivityRule.activity.appContext
        val recordFile = getFakeFile()

        if (recordFile.exists()) {
            recordFile.delete()
        }

        assertTrue("Test file is NOT deleted!", !recordFile.exists())

        mainActivityRule.activity.startRecord(getFakeFile(), MediaRecorder())
        mainActivityRule.activity.stopRecording()

        printRecordDir(currContext)

        assertTrue("Test Record NOT Created", recordFile.exists())
        recordFile.delete()
    }


    @Test
    fun clickStartRecordButton_ShouldProduceRecord() {
        startRecord()
        startRecButton.check(matches(not(ViewMatchers.isEnabled())))
        startRecButton.check(matches(not(ViewMatchers.isDisplayed())))
        stopRecButton.check(matches(ViewMatchers.isEnabled()))
        stopRecButton.check(matches(ViewMatchers.isDisplayed()))
        stopRecord()

    }
}


@RunWith(AndroidJUnit4::class)
class PlayMediaFunctionality {


    @get:Rule
    var mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun playRecord_shouldStartMediaPlayer () {

        val context = mainActivityRule.activity.appContext
        runOnUiThread {
            mainActivityRule.activity.appCollection.mediaPlayer.startPlay(
                getLastRecord(context.filesDir.absolutePath),
                View(context),
                mainActivityRule.activity
            )

            assertTrue(
                "Player is NOT active",
                mainActivityRule.activity.appCollection.mediaPlayer.getPlayer().isPlaying
            )
        }

        mainActivityRule.activity.appCollection.mediaPlayer.stopPlayer()
        assertTrue(
            "Player is active",
            !mainActivityRule.activity.appCollection.mediaPlayer.getPlayer().isPlaying
        )
    }
}


@RunWith(AndroidJUnit4::class)
class DeleteRecordsFunctionality {

    @get:Rule
    var mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun deleteFile_filesShouldBeDelete () {
        val testActivity = mainActivityRule.activity
        val testContext = testActivity.appContext
        val filesDir = testContext.filesDir.absolutePath
        val recToDelete = 3

        if (getRecordsCount(filesDir) <= recToDelete) {
            val recToCreate = 4
            createRecords(recToCreate, testActivity)
        }

        val records = getAllRecords(filesDir)
        val recordsCountBeforeDelete = getRecordsCount(filesDir)
        val firstAfterDeletingRecord =  records[recToDelete]

        runOnUiThread {
            val customAdapter = CustomAdapter(records, testContext, testActivity)
            deleteRecords(recToDelete, customAdapter, records)
        }

        assertTrue(
            "Invalid deleted records count.",
            recordsCountBeforeDelete == getRecordsCount(filesDir) + recToDelete
        )
        assertEquals(getLastRecord(filesDir), firstAfterDeletingRecord)
    }


    fun deleteRecords (recToDelete: Int, customAdapter: CustomAdapter, records: ArrayList<Record> ) {
        val recInd = 0

        for (i in 0 until recToDelete) {
            customAdapter.deleteRecord(records[recInd], recInd)
        }
    }


    fun createRecords (numOfRecords: Int, mainActv: MainActivity) {

        for (x in 0 until numOfRecords) {
            mainActv.startRecord(mainActv.appCollection.getFileInst(), MediaRecorder())
            Thread.sleep(1002)
            mainActv.stopRecording()
        }
    }

}