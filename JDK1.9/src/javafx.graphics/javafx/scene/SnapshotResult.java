/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package javafx.scene;

import javafx.scene.image.WritableImage;

/**
 * This class holds the result of a snapshot operation.
 * @since JavaFX 2.2
 */
public class SnapshotResult {
    private WritableImage image;
    private Object source;
    private SnapshotParameters params;

    // Package scope constructor
    SnapshotResult(WritableImage image, Object source, SnapshotParameters params) {
        this.image = image;
        this.source = source;
        this.params = params;
    }

    /**
     * Gets the image generated by the snapshot operation.
     *
     * @return the generated image
     */
    public WritableImage getImage() {
        return image;
    }

    /**
     * Gets the source Node or Scene on which the snapshot was called.
     *
     * @return the source of the snapshot
     */
    public Object getSource() {
        return source;
    }

    /**
     * Gets a copy of the SnapshotParameters used to generate the snapshot.
     * This will be null in the case of snapshot being called on a Scene.
     *
     * @return a copy of the SnapshotParameters used to perform the snapshot,
     * or null
     */
    public SnapshotParameters getSnapshotParameters() {
        return params;
    }

}
