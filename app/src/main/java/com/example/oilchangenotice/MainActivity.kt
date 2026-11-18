package com.example.oilchangenotice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.oilchangenotice.ui.OilFilterScreen
import com.example.oilchangenotice.ui.theme.OilChangeNoticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OilChangeNoticeTheme {
                OilFilterScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    OilChangeNoticeTheme {
        OilFilterScreen()
    }
}
