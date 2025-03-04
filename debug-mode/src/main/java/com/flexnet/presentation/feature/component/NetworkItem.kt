package com.flexnet.presentation.feature.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.flexnet.R
import com.flexnet.domain.model.HttpInspectorItem
import com.flexnet.domain.model.HttpInspectorStateInterceptor
import com.flexnet.domain.model.Method
import com.flexnet.presentation.foundation.FlexNetTheme
import com.flexnet.presentation.foundation.FlexNetTypography

@Composable
fun NetworkItem(
    modifier: Modifier = Modifier,
    httpInspectorItem: HttpInspectorItem,
) {
    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White),
    ) {
        val (codeRef, contentRef, flagRef) = createRefs()

        Box(
            modifier = Modifier
                .height(96.dp)
                .width(76.dp)
                .background(httpInspectorItem.color)
                .constrainAs(codeRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
        ) {
            when (httpInspectorItem.stateInterceptor) {
                HttpInspectorStateInterceptor.REQUEST -> {
                    Image(
                        painter = painterResource(R.drawable.ic_loading),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.Center),
                    )
                }

                HttpInspectorStateInterceptor.FAILED -> {
                    Image(
                        painter = painterResource(R.drawable.ic_failed),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.Center),
                    )
                }

                HttpInspectorStateInterceptor.SUCCESS -> {
                    Text(
                        text = httpInspectorItem.code.take(3),
                        style = FlexNetTypography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 24.dp),
                    )
                }
            }
        }

        Column(
            Modifier.constrainAs(contentRef) {
                top.linkTo(parent.top, 8.dp)
                start.linkTo(codeRef.end, 8.dp)
                bottom.linkTo(parent.bottom, 8.dp)
                end.linkTo(parent.end, 24.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = httpInspectorItem.url,
                style = FlexNetTypography.titleSmall.copy(color = Color(0xFF2E333D)),
                fontWeight = FontWeight.SemiBold,
                minLines = 2,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = httpInspectorItem.method.name,
                    style = FlexNetTypography.labelMedium.copy(color = Color(0xFF989CA6)),
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = httpInspectorItem.speed,
                    style = FlexNetTypography.labelMedium.copy(color = Color(0xFF989CA6)),
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = httpInspectorItem.timeStamp,
                    style = FlexNetTypography.labelMedium.copy(color = Color(0xFF989CA6)),
                    fontWeight = FontWeight.Normal,
                )
            }
        }

        val showFlag by rememberUpdatedState(httpInspectorItem.isMock)

        if (showFlag) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .size(12.dp)
                    .background(color = Color(0xFF5786E2))
                    .constrainAs(flagRef) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    },
            )
        }
    }
}

@Composable
@Preview
private fun PreviewNetworkItem() {
    FlexNetTheme {
        NetworkItem(
            Modifier,
            HttpInspectorItem(
                code = "200",
                method = Method.DELETE,
                timeStamp = "11:17:52 AM",
                isMock = false,
                color = Color.Black,
                url = "http://www.bing.com/search?q=dictumst",
                speed = "90ms",
                stateInterceptor = HttpInspectorStateInterceptor.REQUEST,
            ),
        )
    }
}
